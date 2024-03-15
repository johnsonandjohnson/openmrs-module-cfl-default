#!/bin/bash

HOME_DIR=~
CFLDEFAULT_OMOD=cfldefault-1.0.0-SNAPSHOT.omod
CFLDEFAULT_OMOD_PREFIX=cfldefault

MODULES_PATH=$HOME_DIR/.cfl-dev/modules
OWA_PATH=$HOME_DIR/.cfl-dev/owa

check_ownership_and_fix () {
  PATH_TO_CHECK=$1
  if [ $(stat -c '%U' $PATH_TO_CHECK) != $(whoami) ] || [ $(find $PATH_TO_CHECK ! -user $(whoami) | wc -l) -gt 0 ]; then
    sudo chown -R $(whoami):$(whoami) $PATH_TO_CHECK
    echo "Changed ownership of $PATH_TO_CHECK to $(whoami):$(whoami)"
  fi
}

mkdir -p $MODULES_PATH
mkdir -p $OWA_PATH &&

check_ownership_and_fix $CFLDEFAULT_REPO &&
check_ownership_and_fix $MODULES_PATH &&

cd $CFLDEFAULT_REPO &&
mvn clean install &&

rm -f $MODULES_PATH/$CFLDEFAULT_OMOD_PREFIX* &&
mv $CFLDEFAULT_REPO/omod/target/$CFLDEFAULT_OMOD $MODULES_PATH &&

cd $CFL_REPO/cfl/ &&
docker-compose down &&
docker-compose up --build -d &&
docker-compose logs -f ||

cd $CFLDEFAULT_REPO/owa
