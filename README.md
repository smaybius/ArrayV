# ArrayV

![Discord](https://img.shields.io/discord/592082838791127075?color=%237289DA&label=Discord&logo=discord&logoColor=white)
![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/Gaming32/ArrayV/maven.yml?branch=main)
![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/Gaming32/ArrayV/checkstyle.yml?branch=main&label=checkstyle)

Over 200 sorting algorithms animated with 15 unique graphic designs

[Join the Discord server!](https://discord.gg/thestudio)

To compile use:
```shell
./mvnw clean verify
```
Or for faster results, install mvnd (https://github.com/apache/maven-mvnd#usage) and use mvnd instead of running the mvnw script, such as:
```shell
mvnd compile
```
This is much faster because it'll only compile data that was changed from the previous build on the same daemon. On the first time though or on a new daemon, it's no different from the standard `clean verify` method.

To run use:
```shell
./run
```
Alternatively, you can double click on "run" or "run.bat" on Windows.

You can always download the most up-to-date pre-built JAR from [GitHub Actions](https://nightly.link/Gaming32/ArrayV/workflows/maven/main/standalone-jar.zip) (link via [nightly.link](https://nightly.link)).



**KNOWN BUGS:**
- Certain sorts (comb sort, radix sorts) cause the program to forget the current speed
- Certain sorts do not work with the "Skip Sort" button
- Missing soundfont
- SkaSort and HolyGrailSort produce errors -- this is normal, they aren't finished yet
- No circular pointer -- will be fixed soon

**PLANS FOR FUTURE RELEASES:**
- SkaSort
- "Holy Grail Sort"
- Options to:
  - Enter in your own set of numbers
  - Select ShellSort gap sequence
  - Change TimSort "minrun" value
  - Change IntroSort threshold for insertion/heap sort
  - Change Simple Shatter Sort rate(?)
  - Stop Run All Sorts(?)
  - Stop TimeSort(?)
- Pre-shuffled arrays
- Organize list of sorts into more categories
- Subheadings for customizable sorts (e.g. display the number of buckets during a bucket sort)
- Fixed circular pointer with much cleaner math
- Toogle between pointer and black bar with circular visuals
- Refactor/reorganize prompts and frames
- Cleaner:
  - Tree Sort
  - getters/setters
  - method parameters
- Small organizational changes
