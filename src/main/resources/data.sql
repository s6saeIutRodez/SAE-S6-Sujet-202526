-- -------------------------------------------------------
-- 1. room_types
-- -------------------------------------------------------
INSERT INTO room_types (id, name, total_rooms) VALUES (1, 'Chambre Simple', 10);
INSERT INTO room_types (id, name, total_rooms) VALUES (2, 'Chambre Double', 8);
INSERT INTO room_types (id, name, total_rooms) VALUES (3, 'Suite',          3);

-- -------------------------------------------------------
-- 2. room_type_prices
-- -------------------------------------------------------
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (1, 1, '2025-01-01', '2025-06-30',  69.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (2, 1, '2025-07-01', '2025-08-31',  89.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (3, 1, '2025-09-01', '2025-12-31',  69.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (4, 2, '2025-01-01', '2025-06-30', 119.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (5, 2, '2025-07-01', '2025-08-31', 149.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (6, 2, '2025-09-01', '2025-12-31', 119.00);
INSERT INTO room_type_prices (id, room_type_id, start_date, end_date, price_per_night) VALUES (7, 3, '2025-01-01', '2025-12-31', 299.00);

-- -------------------------------------------------------
-- 3. inventories  (J+0 à J+14, contrainte UNIQUE room_type_id+date)
-- -------------------------------------------------------
-- Chambre Simple
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (1,  1, CURDATE() + INTERVAL 0  DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (2,  1, CURDATE() + INTERVAL 1  DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (3,  1, CURDATE() + INTERVAL 2  DAY, 10, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (4,  1, CURDATE() + INTERVAL 3  DAY, 10, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (5,  1, CURDATE() + INTERVAL 4  DAY, 10, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (6,  1, CURDATE() + INTERVAL 5  DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (7,  1, CURDATE() + INTERVAL 6  DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (8,  1, CURDATE() + INTERVAL 7  DAY, 10, 2);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (9,  1, CURDATE() + INTERVAL 8  DAY, 10, 2);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (10, 1, CURDATE() + INTERVAL 9  DAY, 10, 2);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (11, 1, CURDATE() + INTERVAL 10 DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (12, 1, CURDATE() + INTERVAL 11 DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (13, 1, CURDATE() + INTERVAL 12 DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (14, 1, CURDATE() + INTERVAL 13 DAY, 10, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (15, 1, CURDATE() + INTERVAL 14 DAY, 10, 0);

-- Chambre Double
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (16, 2, CURDATE() + INTERVAL 0  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (17, 2, CURDATE() + INTERVAL 1  DAY, 8, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (18, 2, CURDATE() + INTERVAL 2  DAY, 8, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (19, 2, CURDATE() + INTERVAL 3  DAY, 8, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (20, 2, CURDATE() + INTERVAL 4  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (21, 2, CURDATE() + INTERVAL 5  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (22, 2, CURDATE() + INTERVAL 6  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (23, 2, CURDATE() + INTERVAL 7  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (24, 2, CURDATE() + INTERVAL 8  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (25, 2, CURDATE() + INTERVAL 9  DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (26, 2, CURDATE() + INTERVAL 10 DAY, 8, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (27, 2, CURDATE() + INTERVAL 11 DAY, 8, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (28, 2, CURDATE() + INTERVAL 12 DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (29, 2, CURDATE() + INTERVAL 13 DAY, 8, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (30, 2, CURDATE() + INTERVAL 14 DAY, 8, 0);

-- Suite
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (31, 3, CURDATE() + INTERVAL 0  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (32, 3, CURDATE() + INTERVAL 1  DAY, 3, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (33, 3, CURDATE() + INTERVAL 2  DAY, 3, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (34, 3, CURDATE() + INTERVAL 3  DAY, 3, 1);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (35, 3, CURDATE() + INTERVAL 4  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (36, 3, CURDATE() + INTERVAL 5  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (37, 3, CURDATE() + INTERVAL 6  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (38, 3, CURDATE() + INTERVAL 7  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (39, 3, CURDATE() + INTERVAL 8  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (40, 3, CURDATE() + INTERVAL 9  DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (41, 3, CURDATE() + INTERVAL 10 DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (42, 3, CURDATE() + INTERVAL 11 DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (43, 3, CURDATE() + INTERVAL 12 DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (44, 3, CURDATE() + INTERVAL 13 DAY, 3, 0);
INSERT INTO inventories (id, room_type_id, date, total_rooms, reserved_rooms) VALUES (45, 3, CURDATE() + INTERVAL 14 DAY, 3, 0);

-- -------------------------------------------------------
-- 4. bookings
-- -------------------------------------------------------
INSERT INTO bookings (id, room_type_id, from_date, to_date, quantity, amount, status, nom, prenom, email)
    VALUES (1, 1, CURDATE() + INTERVAL 2  DAY, CURDATE() + INTERVAL 5  DAY, 1,  207.00, 'CONFIRMED', 'Martin',  'Alice',  'alice.martin@mail.com');

INSERT INTO bookings (id, room_type_id, from_date, to_date, quantity, amount, status, nom, prenom, email)
    VALUES (2, 2, CURDATE() + INTERVAL 10 DAY, CURDATE() + INTERVAL 14 DAY, 1,  476.00, 'PENDING',   'Dupont',  'Bob',    'bob.dupont@mail.com');

INSERT INTO bookings (id, room_type_id, from_date, to_date, quantity, amount, status, nom, prenom, email)
    VALUES (3, 3, CURDATE() + INTERVAL 1  DAY, CURDATE() + INTERVAL 4  DAY, 1,  897.00, 'CONFIRMED', 'Leblanc', 'Claire', 'claire.leblanc@mail.com');

INSERT INTO bookings (id, room_type_id, from_date, to_date, quantity, amount, status, nom, prenom, email)
    VALUES (4, 2, CURDATE() - INTERVAL 5  DAY, CURDATE() - INTERVAL 2  DAY, 2,  714.00, 'CANCELLED', 'Bernard', 'David',  'david.bernard@mail.com');

INSERT INTO bookings (id, room_type_id, from_date, to_date, quantity, amount, status, nom, prenom, email)
    VALUES (5, 1, CURDATE() + INTERVAL 7  DAY, CURDATE() + INTERVAL 10 DAY, 2,  414.00, 'CONFIRMED', 'Moreau',  'Emma',   'emma.moreau@mail.com');

-- -------------------------------------------------------
-- 5. booking_options
-- -------------------------------------------------------
INSERT INTO booking_options (id, booking_id, type, comment) VALUES (1, 1, 'ANNIVERSAIRE', 'Gâteau surprise pour les 30 ans');
INSERT INTO booking_options (id, booking_id, type, comment) VALUES (2, 1, 'FLEUR',        'Bouquet de roses rouges');
INSERT INTO booking_options (id, booking_id, type, comment) VALUES (3, 2, 'LIT BEBE',     'Lit bébé pour un enfant de 18 mois');
INSERT INTO booking_options (id, booking_id, type, comment) VALUES (4, 4, 'AUTRE',        'Demande de chambre côté jardin');
INSERT INTO booking_options (id, booking_id, type, comment) VALUES (5, 5, 'ANNIVERSAIRE', 'Décoration chambre pour anniversaire de mariage');
