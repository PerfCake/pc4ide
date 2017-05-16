# PerfCake-Docs

This module uses Doclet API in order to parse PerfCake JavaDoc from the source code and store it into property file.
Other pc4ide modules use property file in order to provide user with help messages taken
from javadoc.

If you want to parse PerfCake javadoc, then just build this module:

    mvn clean install

parsed property file with javadoc will be located in this module target directory:

    ${PROJECT_ROOT}/perfcake-docs/target/perfcake-javadoc.properties