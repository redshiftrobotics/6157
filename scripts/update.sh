#!/usr/bin/env bash
set -e

if [ "$1" == "--help" ]; then
	echo <<EOF
Usage:

	$0 [--help]

Description:

	Update this repo to the latest version of the FTC SDK from redshiftrobotics/ftc_app. In the
	process, it will create a PR branch based off HEAD.

	If --help is passed, then print this message and exit.

Author:

	Ari Porad (@ariporad) <ariporad@seattleacademy.org>

EOF
	exit
fi

##
# Update this repo to the lastest version from redshiftrobotics/ftc_app.
#
# IMPORTANT: DO NOT RUN THIS IN redshiftrobotics/ftc_app! To update that repo, see the wiki.
# WARNING: As always with git, proceed with caution and make lots of backups.
# WARNING: During this process, you may need to resolve some merge conflicts, but this is unlikely.
##

target_branch="$(git rev-parse --abbrev-ref HEAD)"

function setup_remote() {
	git remote add ftc_app https://github.com/redshiftrobotics/ftc_app
	git fetch ftc_app
}

function setup_update_branch() {
	# The first commit in redshiftrobotics/ftc_app is always a squashed version of the actual SDK
	# All subsequent commits are our modifications. Whenever we need to update it, we do a giant
	# rebase. Therefore, if we want to make a commit with the version number of the SDK, we need the
	# first commit of redshiftrobotics/ftc_app.
	ftc_sdk_commit_message="$(git log "$(git rev-list --max-parents=0 ftc_app/master)")"
	git checkout --orphan tmp/new-ftc-sdk ftc_app/master
	git commit -am "chore: update FTC SDK" -m "$ftc_sdk_commit_message"
}

##
# This is the part of the script that I'm proudest of. Before this, there are two branches:
# - tmp/new-ftc-sdk, which contains a single commit: a squashed version of the current SDK
# - master, who's first commit is a squashed version of the old SDK, and which has other commits too
# Visually:
#             | Old SDK (squashed)
#     master: A - B - C - D
# tmp/new...:             E
#                         ^ New SDK (squashed)
#
# We can't just do a pure merge, because A and E both have very similar content (they are both the
# SDK), and they both think that they created everything. If we just do a pure merge, there won't be
# any actual problems, but there will still be a bunch of "both created" conflicts. That's a PITA.
# To solve this, we make a third branch, `tmp/merge...`, starting at commit A. Then, we merge in E,
# with the recursive strategy, but with the 'theirs' option, meaning that, in the event of a merge
# conflict, E always wins. That's fine, because E is newer than A. That produces a new commit called
# `F`, which we can then safely merge back into master (or a newly created PR branch), because they
# now share a history (`G`). Here's that visually:
#
#               | Old SDK (squashed)
#       master: A - B - C - D ---- G
#               \                 /
# tmp/merge...:  \----------- F --
#                            /
#   tmp/new...:              E
#                            ^ New SDK (squashed)
#
# In the future, we could merge A into E using the 'ours' strategy, which would completely ignore 
# the contents of A and simply replace it with E. I'm not entirely sure if that's desirable or not.
##
function resolve_merge_conflicts() {
	git checkout -b tmp/resolve-merge-conflicts "$(git rev-list --max-parents=0 master)"
	git merge -Xtheirs --allow-unrelated-histories --squash tmp/new-ftc-sdk
	git commit -am "$(git log -1 tmp/new-ftc-sdk)"
}

# Actually merge the now-safe branch into master (actually creates a new PR branch, update-sdk).
function merge() {
	git checkout -b update-sdk "$target_branch"
	git merge tmp/resolve-merge-conflicts
}

function cleanup() {
	git remote rm ftc_app
	git branch -D tmp/new-ftc-sdk
	git branch -D tmp/resolve-merge-conflicts
}

setup_remote
setup_update_branch
resolve_merge_conflicts
merge
cleanup

if command -v hub 2>&1 > /dev/null; then
	cat <<EOF
Successfully updated the FTC SDK!

You're now on a new branch, update-sdk. You can either push this to Github and create a PR:

	git push -u origin update-sdk
	hub pull-request

Or just merge this branch into master directly, delete it, and go about your day:

	git checkout master
	git merge update-sdk
	git branch -d update-sdk
	# You probably want to \`git push\` now!

EOF
else
	cat <<EOF
Successfully updated the FTC SDK!

You're now on a new branch, update-sdk. You can either push this to Github and create a PR:

	git push -u origin update-sdk

Or just merge this branch into master directly, delete it, and go about your day:

	git checkout master
	git merge update-sdk
	git branch -d update-sdk
	# You probably want to \`git push\` now!

EOF
fi
