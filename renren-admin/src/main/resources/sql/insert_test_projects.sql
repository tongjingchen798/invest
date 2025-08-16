-- 插入测试项目数据
INSERT INTO tb_project (
    invest_id, invest_name, abbreviation, status, invest_repeat, project_type, 
    cycle_type, scale_amount, cycle, conversion, principal_profit, total_profit, 
    total_cost, type_id, img, sort, vip, project_explain, create_date, update_date
) VALUES 
(
    '1752821238754', 'Happy Weekend', 'Happy Weekend 2', 1, 1, 0,
    2, 200000, 15, '2', 4000, 60000,
    260000, '1677040832376', 'http://8.216.132.154/images/tools/610bcad6-1eb7-4293-af18-3fdcbd129fcb.png', -10000, 0, 'test\ntest1', NOW(), NOW()
),
(
    '1752821238755', 'Summer Investment', 'Summer Inv', 1, 2, 1,
    1, 500000, 30, '1.5', 7500, 45000,
    545000, '1677040832376', 'http://8.216.132.154/images/tools/default.png', -9000, 1, 'Summer investment project', NOW(), NOW()
),
(
    '1752821238756', 'Winter Fund', 'Winter Fund', 1, 1, 2,
    3, 300000, 45, '2.5', 7500, 33750,
    333750, '1677040832376', 'http://8.216.132.154/images/tools/winter.png', -8000, 0, 'Winter fund project', NOW(), NOW()
),
(
    '1752821238757', 'Spring Growth', 'Spring Growth', 0, 1, 0,
    2, 150000, 20, '1.8', 2700, 54000,
    204000, '1677040832376', 'http://8.216.132.154/images/tools/spring.png', -7000, 2, 'Spring growth project', NOW(), NOW()
),
(
    '1752821238758', 'Autumn Harvest', 'Autumn Harvest', 1, 3, 1,
    1, 800000, 60, '1.2', 9600, 57600,
    857600, '1677040832376', 'http://8.216.132.154/images/tools/autumn.png', -6000, 1, 'Autumn harvest project', NOW(), NOW()
);
