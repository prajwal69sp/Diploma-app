# Security Specification - DiplomaHub AI

## Data Invariants
1. A student profile can only be created and updated by the authenticated user owning that profile.
2. Only users with the 'admin' role can create, update, or delete branches, subjects, units, and resources.
3. Students can only read 'published' branches, subjects, and resources.
4. Admins can read both published and unpublished content.
5. AI usage records are private to the user.
6. Public app settings (tagline, creator bio) are read-only for students, editable only by admins.

## The Dirty Dozen (Vulnerability Test Payloads)
1. Alice attempts to update Bob's profile `role` to 'admin'. (Rejected: Identity)
2. Alice attempts to create a branch. (Rejected: Role-based access)
3. Bob attempts to delete a subject. (Rejected: Role-based access)
4. Alice attempts to read an 'unpublished' resource. (Rejected: Filter alignment)
5. Alice attempts to update a branch's `displayOrder`. (Rejected: Role-based access)
6. Bob attempts to create a resource with a 1MB title string. (Rejected: Size constraint)
7. Alice attempts to inject `isVerified: true` into her profile. (Rejected: Ghost field)
8. Bob attempts to read Alice's AI usage statistics. (Rejected: User isolation)
9. Alice attempts to create a subject with a non-existent `branchId`. (Rejected: Relational sync)
10. Alice attempts to update `createdAt` timestamp on a subject. (Rejected: Immutability)
11. Bob attempts to delete a branch that has active subjects. (Rejected: Child gate)
12. Alice attempts to bypass `isAdmin` check by setting `role: "admin"` in her profile. (Rejected: Admin logic check)

## Admin Bootstrapping
Initial Admin: prajwalmarigowda69@gmail.com
