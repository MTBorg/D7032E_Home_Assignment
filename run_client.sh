classpath=out
inputdir=src
mkdir -p out
javac -d $classpath $inputdir/*.java && java -cp $classpath KingTokyoPowerUpClient 
