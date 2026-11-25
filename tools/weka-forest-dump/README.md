# WEKA forest extraction tool

One-off tool that extracted the trained random forest of the **Developmental Toxicity (CAESAR)** model
(`VegaModels-DevtoxCaesar`) out of WEKA 3.5.8, so that the model can be run without WEKA.

## Why

Up to version 2.1.9 the model carried no trained forest. Its `modelRandomForest` class read
`/data/sel18_EPA_training.arff` and called `buildClassifier` in its constructor, that is, it **trained the
forest again on every instantiation of the model**. Besides the cost, this made the predictions depend on
`weka:weka:3.5.8` — an artifact from 2008 that is not on Maven Central and had to be republished on the
ProtoQSAR Nexus to stay resolvable — and, in principle, on the floating point library of the JVM running the
training.

Since the training is deterministic (`-S 1`), the forest could be extracted once and frozen. From version
2.2.0 the model reads it from `/data/forest_devtox_caesar.txt` and predicts on its own.

The tool is deliberately **not** a module of the `VegaModels-Human` reactor: it is the only place left where
WEKA is declared, and the build of the models must never see it again. It is kept here to document where the
resource comes from and to make the extraction repeatable; nothing depends on it, so it can be deleted.

## What it does

1. Trains the forest exactly as the model used to: same ARFF read from the resources of the model, same
   options `-I 10 -K 0 -S 1 -depth 12`.
2. Walks the trained model by reflection — in WEKA 3.5.8 a `RandomForest` is a `Bagging` (field `m_bagger`) of
   `RandomTree`, and a `RandomTree` is itself the root node of its tree — and writes every node to the text
   format read by `DecisionForest`. It checks the assumptions the extraction rests on and fails if any of them
   does not hold: no `ZeroR` fallback, numeric attributes only, binary splits.
3. Reads the file back and **compares the two implementations bit for bit**, on the whole training set and on
   a large number of generated instances, failing on the first difference. The generated instances include
   every threshold the forest splits on, the two values immediately around it and values outside the range of
   the training set, which is where an implementation of the traversal would part from WEKA's.

The prediction being reproduced is, from the bytecode of WEKA 3.5.8:

- `RandomTree.distributionForInstance`: a split node sends the values below its threshold to the first
  successor; a leaf, **or a split node whose subtree returns nothing**, returns its own stored `m_ClassProbs`,
  which is not normalized again. Hence the resource stores a distribution for the inner nodes too.
- `Bagging.distributionForInstance`: the distributions of the trees are summed, then normalized unless their
  sum is zero up to `Utils.SMALL` (1e-6).
- `Classifier.classifyInstance`: the first index holding the maximum wins, so a tie goes to class `N`, and a
  distribution with no positive mass predicts nothing at all — which the model reported as `N`.

## Running it

The model must be installed first, since the tool reads the training set and `DecisionForest` from its jar:

```
cd ..\..
mvn install -DskipTests -pl VegaModels-DevtoxCaesar
cd tools\weka-forest-dump
mvn compile dependency:build-classpath -Dmdep.outputFile=target/cp.txt
java -cp "target/classes;$(cat target/cp.txt)" insilico.tools.WekaForestDump <output file> [instances]
```

`instances` is the number of generated instances to check, 200000 by default. Set `devtox.version` in the pom
if the model's version has moved on.

The output replaces `VegaModels-DevtoxCaesar/src/main/resources/data/forest_devtox_caesar.txt`. Two runs in
separate JVMs produce byte-identical files; it was checked when the resource was first extracted.
