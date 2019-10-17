classpath=out
inputdir=src
mkdir -p $classpath
javac -d $classpath $(find . -name "*.java") && java -cp $classpath KingTokyoPowerUpServer
