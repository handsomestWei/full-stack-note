-- Enable PostGIS (as of 3.0 contains just geometry/geography)
-- 启用PostGIS功能（仅包括geometry/geography相关）
CREATE EXTENSION postgis;

-- enable raster support (for 3+)
-- 启用栅格扩展
CREATE EXTENSION postgis_raster;

-- Enable Topology
-- 启用拓扑扩展
CREATE EXTENSION postgis_topology;

-- Enable PostGIS Advanced 3D
-- and other geoprocessing algorithms
-- sfcgal not available with all distributions
CREATE EXTENSION postgis_sfcgal;
-- fuzzy matching needed for Tiger
CREATE EXTENSION fuzzystrmatch;
-- rule based standardizer
CREATE EXTENSION address_standardizer;
-- example rule data set
CREATE EXTENSION address_standardizer_data_us;
-- Enable US Tiger Geocoder
CREATE EXTENSION postgis_tiger_geocoder;

-- 查看当前启用的PostGIS版本
SELECT postgis_full_version();