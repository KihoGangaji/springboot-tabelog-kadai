-- shops テーブル
INSERT IGNORE INTO shops (id, shop_name, image_name, description, price, reserve_cnt, address, phone_number, created_at, updated_at, category_id) 
VALUES 
(1, 'SAMURAIハイム', 'お店１.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 6000, 30, '埼玉県春日部市飯沼X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 1),
(2, 'ヴィラSAMURAI', 'お店２.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 7000, 20, '千葉県市川市高谷新町X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 2),
(3, 'SAMURAIパレス', 'お店３.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 8000, 30, '東京都墨田区立川X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 3),
(4, 'ロッジ SAMURAI', 'お店４.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 9000, 20, '神奈川県横浜市保土ヶ谷区星川X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 1),
(5, 'SAMURAI', 'お店５.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 10000, 30, '新潟県新潟市駒込X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 2),
(6, 'SAMURAI館', 'お店６.jpg', '最寄り駅から徒歩10分。自然豊かで閑静な場所にあります。長期滞在も可能です。', 10000, 20, '新潟県新潟市駒込X-XX-XX', '012-345-678', '2024-12-12 00:00:00', '2024-12-24 00:00:00', 3);

-- roles テーブル
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- users テーブル
INSERT IGNORE INTO users (id, name, furigana, address, phone_number, email, password, role_id, enabled) 
VALUES 
(1, '侍 太郎', 'サムライ タロウ', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'taro.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true),
(2, '侍 花子', 'サムライ ハナコ', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'hanako.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true),
(3, '侍 義勝', 'サムライ ヨシカツ', '奈良県五條市西吉野町湯川X-XX-XX', '090-1234-5678', 'yoshikatsu.samurai@example.com', 'password', 1, false),
(4, '侍 幸美', 'サムライ サチミ', '埼玉県吉川市南広島X-XX-XX', '090-1234-5678', 'sachimi.samurai@example.com', 'password', 1, false),
(5, '侍 雅', 'サムライ ミヤビ', '滋賀県東近江市佐目町X-XX-XX', '090-1234-5678', 'miyabi.samurai@example.com', 'password', 1, false),
(6, '侍 正保', 'サムライ マサヤス', '宮城県柴田郡大河原町旭町X-XX-XX', '090-1234-5678', 'masayasu.samurai@example.com', 'password', 1, false),
(7, '侍 真由美', 'サムライ マユミ', '新潟県新潟市松岡町X-XX-XX', '090-1234-5678', 'mayumi.samurai@example.com', 'password', 1, false),
(8, '侍 安民', 'サムライ ヤスタミ', '神奈川県横浜市旭区今川町X-XX-XX', '090-1234-5678', 'yasutami.samurai@example.com', 'password', 1, false),
(9, '侍 章緒', 'サムライ アキオ', '広島県東広島市高屋町宮領X-XX-XX', '090-1234-5678', 'akio.samurai@example.com', 'password', 1, false),
(10, '侍 祐子', 'サムライ ユウコ', '京都府南丹市美山町高野X-XX-XX', '090-1234-5678', 'yuko.samurai@example.com', 'password', 1, false),
(11, '侍 秋美', 'サムライ アキミ', '京都府京都市左京区田中西春菜町X-XX-XX', '090-1234-5678', 'akimi.samurai@example.com', 'password', 1, false),
(12, '侍 信平', 'サムライ シンペイ', '兵庫県加東市新定X-XX-XX', '090-1234-5678', 'shinpei.samurai@example.com', 'password', 1, false);

-- reservationsテーブル
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (1, 1, 1, '2024-10-18 17:00:00', 2);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (2, 2, 1, '2024-10-18 17:00:00', 3);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (3, 3, 1, '2024-10-18 17:00:00', 4);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (4, 4, 1, '2024-10-18 17:00:00', 5);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (5, 5, 1, '2024-10-18 17:00:00', 6);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (6, 6, 1, '2024-10-18 17:00:00', 2);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (7, 7, 1, '2024-10-18 17:00:00', 3);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (8, 8, 1, '2024-10-18 17:00:00', 4);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (9, 9, 1, '2024-10-18 17:00:00', 5);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (10, 10, 1, '2024-10-18 17:00:00', 6);
INSERT IGNORE INTO reservations (id, shop_id, user_id, reserve_date_time, number_of_people) VALUES (11, 11, 1, '2024-10-18 17:00:00', 2);