-- 添加VIP表索引以优化查询性能

-- 为维护人ID创建索引，提高按维护人查询的性能
CREATE INDEX idx_维护人id ON vip (维护人id);

-- 为维护人ID和未消费天数创建联合索引，优化"念念不忘"和"好久不见"会员查询
CREATE INDEX idx_维护人id_未消费天数 ON vip (维护人id, 未消费天数);

-- 为M值创建索引，优化按M值排序的查询
CREATE INDEX idx_M ON vip (M);

-- 为HYID创建索引，优化按会员ID查询的性能
CREATE INDEX idx_hyid ON vip (hyid);

-- 为name创建索引，优化按会员名称查询的性能
CREATE INDEX idx_name ON vip (name); 