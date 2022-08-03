-- Create table with spatial column
-- 创建一个表mytable，包含了一个空间列geom，格式是GEOMETRY(Point,26910)
-- 这里的26910是一个SRID
CREATE TABLE mytable (
  id SERIAL PRIMARY KEY,
  geom GEOMETRY(Point, 26910),
  name VARCHAR(128)
);
 
-- Add a spatial index
-- 给geom列添加一个空间索引，索引类型为GIST
CREATE INDEX mytable_gix
  ON mytable
  USING GIST (geom);
 
-- Add a point
-- 插入一个数据点
INSERT INTO mytable (geom) VALUES (
  ST_GeomFromText('POINT(0 0)', 26910)
);
 
-- Query for nearby points
-- 查询与给定点周围1000米的点
SELECT id, name
FROM mytable
WHERE ST_DWithin(
  geom,
  ST_GeomFromText('POINT(0 0)', 26910),
  1000
);
