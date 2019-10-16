classpath = out
mkdir -p out
javac -d $classpath KingTokyoPowerUpClient.java && KingTokyoPowerUpClient -cp $classpath
