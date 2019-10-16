classpath=out
inputdir=src
mkdir -p $classpath
javac -d $classpath $inputdir/*.java && java -cp $classpath KingTokyoPowerUpServer
