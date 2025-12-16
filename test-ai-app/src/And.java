bash
# Switch to master branch (create if doesn't exist)
git checkout -b master

# Or if master already exists, just switch to it
git checkout master

# Delete main branch locally
git branch -d main

# Delete main branch from remote
git push origin --delete main

# Push master branch to remote
git push -u origin master
```

If you need this as a shell script file:

```bash
#!/bin/bash

# Create and checkout to master branch if it doesn't exist, otherwise switch to it
git checkout master 2>/dev/null || git checkout -b master

# Delete main branch locally if it exists
git branch -D main 2>/dev/null

# Delete main branch from remote
git push origin --delete main 2>/dev/null

# Push master branch to remote with upstream tracking
git push -u origin master

echo "Successfully switched to master branch and deleted main branch"