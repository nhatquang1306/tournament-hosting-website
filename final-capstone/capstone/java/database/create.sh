#!/bin/bash

export PGPASSWORD='postgres1'
BASEDIR=$(dirname $0)
DATABASE=tournament-db

export PGPORT=15432
psql -U postgres -p $PGPORT -f "$BASEDIR/dropdb.sql" &&
createdb -U postgres -p $PGPORT $DATABASE &&
psql -U postgres -d $DATABASE -p $PGPORT -f "$BASEDIR/schema.sql" &&
psql -U postgres -d $DATABASE -p $PGPORT -f "$BASEDIR/data.sql" &&
psql -U postgres -d $DATABASE -p $PGPORT -f "$BASEDIR/user.sql"

#export PGPASSWORD='postgres1'
#BASEDIR=$(dirname $0)
#DATABASE=tournament-db
#psql -U postgres -f "$BASEDIR/dropdb.sql" &&
#createdb -U postgres $DATABASE &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/schema.sql" &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/data.sql" &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/user.sql"

