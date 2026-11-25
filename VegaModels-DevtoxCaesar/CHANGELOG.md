# Changelog

History of the Developmental Toxicity model (CAESAR), `InSilico:VegaModels-DevtoxCaesar`. Each model module of
this repository keeps its own changelog, since each one carries its own version. The history starts with 2.2.0:
the changes of the earlier versions were not recorded.

## 2.2.0 - 2026-11-25

- **WEKA is gone.** The model no longer depends on `weka:weka:3.5.8`, an artifact from 2008 that is not on Maven
  Central and was served only by the ProtoQSAR Nexus, where it had to be republished to stay resolvable.
- Up to 2.1.9 the module carried no trained model at all: `modelRandomForest` called `buildClassifier` in its
  constructor, that is, it **trained the random forest again on every instantiation of the model**
  (`RandomForest -I 10 -K 0 -S 1 -depth 12` on `/data/sel18_EPA_training.arff`). Besides the cost, this made the
  predictions depend on the floating point library of the JVM that ran the training, so the same molecule could in
  principle be classified differently on different platforms.
- The training being deterministic, the forest was extracted once and frozen in the new resource
  `/data/forest_devtox_caesar.txt`: 672 nodes over 10 trees, one node per line, each tree in preorder. The new
  `insilico.devtox_caesar.model.DecisionForest` reads it and predicts on its own, reproducing WEKA's behaviour
  exactly: the class distribution stored in the node reached is returned as is, the distributions of the trees are
  summed and normalized unless their sum is zero up to 1e-6, and the first index holding the maximum wins, so a
  tie goes to the non toxic class.
- The extraction was done by the new `tools/weka-forest-dump`, which is deliberately not a module of the reactor:
  it is the only place left where WEKA is declared, and the build of the models never sees it. The same tool
  compared the two implementations bit for bit on the whole training set and on 200000 generated instances,
  including every threshold the forest splits on and the values one ULP around it, finding no difference.
- `CalculateModel` no longer writes an ARFF file in memory to hand the descriptors over to WEKA, and the order the
  descriptors had to be written in is no longer hard coded: the forest matches its attributes with the descriptors
  of the model by name.
- `sel18_EPA_training.arff` is kept in the resources, unchanged, as the data the forest was trained from; it is no
  longer read when the model runs.
- **No prediction changes**: the regression tests pass against the references of 2.1.9, which were not
  regenerated, on 292 training molecules and 601 molecules of the core dataset. Instantiating the model takes
  4-7 ms instead of 16-28 ms, since nothing is trained any more.
