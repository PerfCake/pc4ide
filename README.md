# pc4ide

pc4ide's goal is to create graphical editor for [PerfCake](https://www.perfcake.org/) scenarios.

However it is not intended to use this editor directly (although it is possible), because it wouldn't bring you the full functionality needed for scenario editor. It is expected to use pc4ide as a plugin in your favourite IDE (Eclipse, Idea and NetBeans will be supported). pc4ide's goal is to create common codebase for individual IDE plugins.

## Contributing

We would very appreciate if you decide to contribute. There are many opportunities such as sending pull request or reporting issue with bug or possible enhancement.

### Reporting issues
We don't have resources to verify all functionality, if something doesn't work for you please let us know, so that we may fix it.

Similarly, if you have any ideas on new functionality of pc4ide or some features enhancement, please let us know using issues in this repository.

### Sending a pull request

If something is missing or not working for you and you know how to fix it, we will appreciate that even more.

However, please follow our checkstyle rules for your code. You may check your code validity by invoking maven checkstyle plugin from the root of this repository:

    mvn checkstyle:check

In the repository, there is also prepared git hook which checks your commits and doesn't allow you to commit code which does not satisfy checkstyle rules. You may install it using following command:

    ./checkstyle/install-git-hook.sh



