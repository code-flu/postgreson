-- TODO: Update `postgres-sql-db-manager` image to allow permissions to create SCHEMA
-- CREATE SCHEMA IF NOT EXISTS lens;

-- Lens

CREATE OR replace VIEW public._7998808de855e0ebc35871ad2597cc35
AS
  SELECT (json -> 'medications') AS medications,
 (json -> 'labs') AS labs,
 (json -> 'imaging') AS imaging
  FROM   public.json_b WHERE id='7998808de855e0ebc35871ad2597cc35';

CREATE OR REPLACE VIEW public.view_lens AS
SELECT
  name AS lens_name, contract,
  description,
  owner,
  tags,
  published,
  updated_at,
  updated_by,
  version
FROM
  public.lens
WHERE
  deleted = false;-- Remove deleted


-- Entity
CREATE OR REPLACE VIEW public.view_entity AS
WITH entity AS (
  SELECT
    name AS lens_name,
    (json -> 'entities') AS entities
  FROM
    public.lens
)
SELECT
  entity.lens_name,
  attr.name as entity_name,
  attr.extend, attr.description
FROM
  entity,
  jsonb_to_recordset(entity.entities) AS attr(
    name VARCHAR, extend VARCHAR, description VARCHAR
  );


-- Field
CREATE OR replace VIEW public.view_field AS
WITH entity AS (
  SELECT
    name AS lens_name,
    (json -> 'entities') AS entities
  FROM
    public.lens
), field AS (
SELECT
       entity.lens_name,
       attr.name as entity_name,
       attr.fields
FROM   entity,
       jsonb_to_recordset(entity.entities) AS attr(name VARCHAR, fields jsonb)
 )
SELECT
       field.lens_name,
       field.entity_name,
       attr.name,
       attr.type,
       attr.primary,
       attr.description,
       attr.sql_snippet
FROM   field,
       jsonb_to_recordset(field.fields) AS attr(name VARCHAR, type VARCHAR, "primary" BOOLEAN, description TEXT, sql_snippet VARCHAR);



-- Measure
CREATE OR replace VIEW public.view_measure AS
WITH entity AS (
  SELECT
    name AS lens_name,
    (json -> 'entities') AS entities
  FROM
    public.lens
), measure AS (
SELECT
       entity.lens_name,
       attr.name as entity_name,
       attr.MEASURES
FROM   entity,
       jsonb_to_recordset(entity.entities) AS attr(name VARCHAR, MEASURES jsonb)
 )
SELECT
       measure.lens_name,
       measure.entity_name,
       attr.name,
       attr.type,
       attr.alien,
       attr.hidden,
       attr.depends,
       attr.description,
       attr.sql_snippet,
       attr.rolling_window
FROM   measure,
       jsonb_to_recordset(measure.measures) AS attr(name VARCHAR, type VARCHAR, alien BOOLEAN, hidden BOOLEAN, depends TEXT[], description TEXT, sql_snippet VARCHAR, rolling_window VARCHAR);


-- Dimension
CREATE OR replace VIEW public.view_dimension AS
WITH entity AS (
  SELECT
    name AS lens_name,
    (json -> 'entities') AS entities
  FROM
    public.lens
), dimension AS (
SELECT
       entity.lens_name,
       attr.name as entity_name,
       attr.dimensions
FROM   entity,
       jsonb_to_recordset(entity.entities) AS attr(name VARCHAR, dimensions jsonb)
 )
SELECT
       dimension.lens_name,
       dimension.entity_name,
       attr.name,
       attr.type,
       attr.alien,
       attr.hidden,
       attr.sub_query,
       attr.description,
       attr.sql_snippet
FROM   dimension,
       jsonb_to_recordset(dimension.dimensions) AS attr(name VARCHAR, type VARCHAR, alien BOOLEAN, hidden BOOLEAN, sub_query TEXT, description TEXT, sql_snippet VARCHAR);


-- Relationship
CREATE OR replace VIEW public.view_relationship AS
WITH entity AS (
  SELECT
    name AS lens_name,
    (json -> 'entities') AS entities
  FROM
    public.lens
), relationship AS (
SELECT
       entity.lens_name,
       attr.name as entity_name,
       attr.relationships
FROM   entity,
       jsonb_to_recordset(entity.entities) AS attr(name VARCHAR, relationships jsonb)
 )
SELECT
       relationship.lens_name,
       relationship.entity_name AS source_entity_name,
       attr.type AS relation_type,
       attr.field AS source_field_name,
       (attr.target ->> 'name') AS target_entity_name,
       (attr.target ->> 'field') AS target_field_name,
       attr.sql_snippet AS custom_sql_snippet,
       attr.verified,
       attr.description
FROM   relationship,
       jsonb_to_recordset(relationship.relationships) AS attr(type VARCHAR, field VARCHAR, target JSONB, verified BOOLEAN, description TEXT, sql_snippet VARCHAR);

