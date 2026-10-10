const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const DATABASE_ID = "ai-studio-android-diplomah-1e1155df-ea8d-47c6-b1f8-dea267eda6a4";
const ALICE_UID = "alice_123";
const ALICE_EMAIL = "alice@test.com";
const ADMIN_EMAIL = "prajwalmarigowda69@gmail.com";

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: "127.0.0.1",
      port: 8085,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore(DATABASE_ID);
  await assertFails(unauthDb.collection("profiles").get());
});

test("Authenticated student: can create their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: ALICE_EMAIL }).firestore(DATABASE_ID);
  await assertSucceeds(aliceDb.collection("profiles").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: ALICE_EMAIL,
    displayName: "Alice",
    role: "student",
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Authenticated student: cannot create profile with admin role", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: ALICE_EMAIL }).firestore(DATABASE_ID);
  await assertFails(aliceDb.collection("profiles").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: ALICE_EMAIL,
    displayName: "Alice",
    role: "admin",
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("Specific email (Admin): can create a branch", async () => {
  const adminDb = testEnv.authenticatedContext("admin_uid", { email: ADMIN_EMAIL }).firestore(DATABASE_ID);
  await assertSucceeds(adminDb.collection("branches").doc("cs").set({
    name: "Computer Science",
    description: "CS branch",
    iconName: "computer",
    displayOrder: 1,
    isEnabled: true,
    semesterCount: 6
  }));
});

test("Regular student: cannot create a branch", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: ALICE_EMAIL }).firestore(DATABASE_ID);
  await assertFails(aliceDb.collection("branches").doc("cs").set({
    name: "Computer Science",
    description: "CS branch",
    iconName: "computer",
    displayOrder: 1,
    isEnabled: true,
    semesterCount: 6
  }));
});
