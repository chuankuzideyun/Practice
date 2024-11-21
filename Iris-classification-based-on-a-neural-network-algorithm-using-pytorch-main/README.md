# Iris classification based on a neural network algorithm using pytorch

# 1. Introduction
 
This project uses the pytorch deep learning framework to implement a classification task for the Iris dataset based on a neural network algorithm.
 
# 2. Dataset
The Iris dataset is a commonly used dataset for classification experiments and was collected by Fisher, 1936.Iris, also known as the Iris flower dataset, is a class of dataset for multivariate analysis. The dataset contains 150 data samples divided into 3 classes of 50 data each, each containing 4 attributes. The four attributes, calyx length, calyx width, petal length, and petal width, can be used to predict which of the three classes (Setosa, Versicolour, and Virginica) the iris flower belongs to.

The specific data is detailed in the file Iris_data.txt

The read method for the Iris flower dataset is customized, inherited from pytorch's Dataset class, see file data_loader.py for details.

# 3. Fully connected network
Model building, training, validation and testing are detailed in the file fully_connected_network.py 
 
 <img src="https://user-images.githubusercontent.com/102544244/211217925-3b96de9c-48a1-4463-b328-3f73b820a85d.png" width="600px">
