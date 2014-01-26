(
u = UnitTest.new;
u.test("that true is true", {
  assert {true};
  assert {0 < 1;};
  assert {not(false)};
});
)

/* Prints the following:

Testing: that true is true
Assertions: 3	 Successes: 2	 Failures: 1

*/

(
u = UnitTest.new;

// setup initializes any variables in the environment
u.setup {
  arg en;
  en[\zero] = 0; // Add a variable \zero to the test environment
  en;
};

// takedown returns the environment to how it was before
u.takedown {
  arg en;
  en[\zero] = nil; // Remove the variable from the test environment
  en;
};

// test runs setup, then takedown
u.test("that zero exists", {
  arg en;
  assert {en[\zero] < 1};
});
)

(
u = UnitTest.new;

u.test("that asserting equality is okay", {
  // One way of writing a two-argument assertion
  {1 - 1}.assertEqual(0);
  // Another way of writing it
  assertEqual({1 - 1}, 0);
});
)

(
u = UnitTest.new;

u.test("that assertions in delta work", {
  assertInDelta({2.0 - 1.0}, 0.95, 0.1);
});
)
