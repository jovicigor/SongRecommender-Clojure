# Song Recommendation Algorithms
A Clojure library for clustering, classifying and calculating similarities between songs using a dataset obtained from Spotify.com.  
## Overview
This library is a central component of a Song Recommender application (https://github.com/Igor-Jovic/SongRecommenderWeb).
It implements few core algorithms:
1. K-means algorithm - used for clustering (grouping) songs. 
2. Various similarity measures - Euclidean distance, Euclidean similarity, Jaccard Similarity... 

The dataset of around 750.000 songs can be found on### Prerequisites

In order to use this tool Song Database is needed along with necessary .csv files which can be found here 
https://drive.google.com/drive/folders/0B3MZlBNxyLNuQzd0bFIwbzN6LXM?usp=sharing and it consists of features described in the table below. 

| Attribute      | type           | description               |
| :-------------- |:--------------| :-------------------------|
| remote_id | string | The Spotify ID of a song. |
| acousticness   | float          | A confidence measure from 0.0 to 1.0 of whether the track is acoustic. 1.0 represents high confidence the track is acoustic. |
| danceability   | float          | Danceability describes how suitable a track is for dancing based on a combination of musical elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is least danceable and 1.0 is most danceable. |
| energy         | float          | Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity. Typically, energetic tracks feel fast, loud, and noisy. For example, death metal has high energy, while a Bach prelude scores low on the scale. Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate, and general entropy. |
| instrumentalness| float          | Predicts whether a track contains no vocals. "Ooh" and "aah" sounds are treated as instrumental in this context. Rap or spoken word tracks are clearly "vocal". The closer the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content. Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as the value approaches 1.0. |
| key| int | The key the track is in. Integers map to pitches using standard Pitch Class notation. E.g. 0 = C, 1 = C♯/D♭, 2 = D, and so on.|
| liveness| float | 	Detects the presence of an audience in the recording. Higher liveness values represent an increased probability that the track was performed live. A value above 0.8 provides strong likelihood that the track is live.|
| mode| int | Mode indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived. Major is represented by 1 and minor is 0.|
| speechiness| float |Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks.|
| tempo | float | The overall estimated tempo of a track in beats per minute (BPM). In musical terminology, tempo is the speed or pace of a given piece and derives directly from the average beat duration.|
| valence | float |	A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric), while tracks with low valence sound more negative (e.g. sad, depressed, angry).|
| album_year | int | The year of the album release. |
| genres | string |	A list of genres associated with the song.|

## Getting Started

To use this library simply clone the project and build it with Leiningen (https://leiningen.org/):
- lein compile
- lein uberjar
 
Now you can include the jar into your normal java project and use the generated classes:
1. com.songrecommender.SongRecommenderCore - this class is used for clustering the songs - performClustering method returns a Map which holds list of song ids for each centroid id. 

```java
 Map<String, List<String>> clusteringResult = new SongRecommenderCore()
                                                 .performClustering(numberOfClusters, pathToCsv);
```
Usage examples can be found on https://github.com/Igor-Jovic/SongRecommenderClustering 

2. com.songrecommender.Classifier - used for classification of a new song, finding most similar one and finding top similar songs. 

```java
String findCentroid() {
        return new Classifier()
                   .findClosestCentroids(centroidsCsvPath, minValuesCsvPath, maxValuesCsvPath, songMap);
    }

    String findSimmilarByEuclidean(int cluster) {
        return new Classifier()
                  .findSimilarWithEuclideanDistance("Cluster" + cluster + ".csv", minValuesCsvPath, maxValuesCsvPath, songMap);
    }

    List findTopMatches(int cluster, int numberOfMatches) {
        return new Classifier()
                  .getTopMatches("Cluster" + cluster + ".csv", minValuesCsvPath, maxValuesCsvPath, songMap, numberOfMatches);
    }
```

Usage examples can be found on https://github.com/Igor-Jovic/SongRecommenderWeb (service layer)

## Typical recommendation workflow
1. All songs are grouped using kmeans algorithm.
2. The dataset is splitted into separate .csv files, by cluster.
3. New song with attributes is passed to findCentroid() method, it is then assigned to the closest centroid's cluster.
4. From that cluster, the top similar songs are extracted and their ID's are returned.
5. Ids can be used to query Spotify.com to get Song info (name, artist, preview url, etc.) 
