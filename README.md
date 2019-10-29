# Running
The server can be run by executing the file run_server.sh 

`bash run_server.sh`

or by running

`javac $(find src -name "*.java") && java src.server.Server`

The client bot can be run by executing the file run_client.sh 

`bash run_client.sh`

or by running

`javac $(find src -name "*.java") && java src.client.KingTokyoPowerUpClient bot`

to start a human player client run (you need to compile first)

`java src.client.KingTokyoPowerUpClient`

# Testing
Unit tests can be run by runnig the file unittests.sh

`bash unittests.sh`

or by running

`javac -cp ./lib/org.junit4-4.3.1.jar $(find . -name "*.java") && \
echo "Running unittests..." && \
java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:. org.junit.runner.JUnitCore tests.AllTests`

Due to a suspected bug in JUnit one test fails the first time when compiling, but should
work when compiling the second time.
