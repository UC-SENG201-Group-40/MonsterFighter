# UC Software Engineering Project Report

### By
- David Liang
- Vincent (`vno16`, `66346377`)

### Structure of the application

The application source code itself is divided into 2 main parts, the `model` and the `ui`. The `model` code is self-containted and include all the logical part of the application. In `model` portion, we decided the classes are divided into an overall `GameManager`, a couple smaller `manager`-like classes that provide certain game features (i.e. inventory, battling, monster party, etc.), and a base object that describe game objects. 

The base object only includes `Monster` and `Item`. We implemented both using abstract classes so that use subclassing to create multiple types of `Monster` or `Item`. We didn't go with interface because for both `Monster` and `Item`, there are a lot of common functionality and grouping those methods as non-abstract methods in the superclass make sense for us. Furthermore, we can also take advantage of abstract class such as override those common methods for certain types of monster / item. For instance, the `Eel` monster type override the base / superclass `damage` method to heal the `1/4` amount of damage dealt.

A big decision we had to make is about the `BattleManager`, we decided that we want the battle to be automatic and does not require the user to actively interace with the monster. We realize that the battle require a constant loop with a delay, but the GUI and the CLI will require different way of creating a delayed loop. Therefore, we decided to make the `BattleManager` only provide a method that needed to be called on each iteration and require the UI to implement an interface for callbacks on each type event that could happen during the battle, while the specific UI will only call that method with their own implementation of a loop. 


### Unit test coverage

<img src="https://delivery-exclaimation-30760d.netlify.app/images/others/coverage0.png" alt="coverage" height="100"/>

We managed to get a good 100% coverage for the classes and methods. We reach this high percentage by making sure that all our test code covers all possible and realistic cases where each class and method are used which include common edge cases. However, we didn't get the 100% coverage looking from the point of view of each line of code. We tried to make sure the test covers area that makes sense in making sure the code works properly, instead of just trying to reach a high percentage in the coverage. There are certain if statements (guard statements) in some methods that are not tested, mostly due them being very simple or have similar logic to other similar cases. We felt that testing those lines would relatively difficult to make meaningful.

<!-- Don't go over the 23 line -->

---

### Thoughts and Feedback

#### Vincent
I personally feel that this project is a very nice and fun way of applying the course materials. The only minor complain is that it feel like that the labs and the lectures in the second half of the semester are a little bit confusing because of the project, for instance some the labs focused on topics for the GUI and the lectures were discussing something else. 

#### David

### Brief retrospective on the project

The big thing that went wrong was that we often over-estimate the time we have. This particularly visible in our CLI which was rushed at the end. We could have prepared a bit better in allocating time to certain time.

For the battling, We were having difficulty visiualizing the process in a interesting manner. We ended with a implementation where the `BattleManager` keep track of its progression system and have the UI provide callbacks it can be notified if certain events happened. While this new approach worked suprisingly well with the GUI, it was definitely rushed, fairly complicated, and something you would argue could be improved.

To sum up, most of the project went fairly well. While there are some small issues here and there, we were able to tackle them rather easily. The choices of using git early and for all part of the project helped speed up our work.

### Effort spent 

#### Vincent
- `86-96` hours in total spent
    - `20-24` hours the first 2 weeks, spent **planning** and **designing**
    - `32-36` hours during the 3 weeks break, spent **implementing** the `model` classes and unit test
    - `16` hours the week after the break, spent **implementing** the `GUI` classes and unit test for the `CLI`
    - `18-20` hours the last 2 weeks, spent **making necessary changes** that the project met the requirements

#### David


### Agreed contribution

We didn't actually agreed on a strict % contribution. We just agreed that we both will try to reach a good portion of contributon, somwhere around `40%-60%` (optimally `50%`) for every part of the project. We didn't have either contribute stricly `50%` of the work because it is hard to be split the work evenly and we felt that it was unnecessary and not very productive.
