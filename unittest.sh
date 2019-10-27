javac -cp ./lib/org.junit4-4.3.1.jar $(find . -name "*.java") && \
echo "Running unittests..." && \
java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:. org.junit.runner.JUnitCore tests.AllTests

# TODO: This only causes errors for now, but might be nice to have eventually
# echo "Generating report..."
# java -jar ./lib/org.jacoco.examples-0.7.7.jar .
