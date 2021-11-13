Planning horizon :

- W weeks
- D Days
- 2 timeslots per day (morning/afternoon)

There is a total of W x D x 2 time slots.

Courses :

- N courses
- Each course has M lectures

2 models : boolean and integer

Time slot index : w * (D*2) + d * 2 + s

Boolean :

Let X<sub>ij</sub> a boolean variable that indicates that a lecture of course j is scheduled on time slot i.

Integer :

For each course, there are 3 arrays :

- w<sub>jk</sub> indicates that the k-th lecture of course j  is scheduled on week w_<sub>kj</sub>
- d<sub>kj</sub> : for the day
- s<sub>kj</sub> : for morning/afternoon
