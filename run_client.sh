classpath=out
inputdir=src
mkdir -p out
javac -d $classpath $(find . -name "*.java") && java -cp $classpath KingTokyoPowerUpClient
