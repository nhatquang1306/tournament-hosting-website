##!/bin/bash
#export PGPASSWORD='postgres1'
#BASEDIR=$(dirname $0)
#DATABASE=tournament-db
#psql -U postgres -f "$BASEDIR/dropdb.sql" &&
#createdb -U postgres $DATABASE &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/schema.sql" &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/data.sql" &&
#psql -U postgres -d $DATABASE -f "$BASEDIR/user.sql"

export PGPASSWORD='cg3aDe6b*E*1C5E3Ad16aGcCAAf-bcaf'
BASEDIR=$(dirname $0)
psql -h roundhouse.proxy.rlwy.net -U postgres -p 27906 -d railway -f "$BASEDIR/dropdb.sql" &&
psql -h roundhouse.proxy.rlwy.net -U postgres -p 27906 -d railway -f "$BASEDIR/schema.sql" &&
psql -h roundhouse.proxy.rlwy.net -U postgres -p 27906 -d railway -f "$BASEDIR/data.sql" &&
psql -h roundhouse.proxy.rlwy.net -U postgres -p 27906 -d railway -f "$BASEDIR/user.sql"

