---
layout: default
title: Addendum Concerns and Decisions
---

# Addendum Concerns and Decisions

## Tracking Versus Migrating

Sometimes you need to track changes. I'm on a project now where sometimes I'm
going to update the databaes, sometimes I'm going to accept that someone else
updated. If I update it, I want to use addendeum. Why not? It writes the SQL for
me.

I'm considering how to keep from stomping on someone else's updates, probably
the best thing to do is to take the migrations out of startup, and then only run
them if you know what you are doing.

But, other thoughts are marking skips, but suddenly, I realize that I can skip
all the way out to the most recent adjustment, maybe even use a negative index
at the outset, that would be easier.

Then, there is the notion of writing the updates to a DDL file, so that you can
see what will be done, with commments that say things like, "Execut Java block."
when that occours.
