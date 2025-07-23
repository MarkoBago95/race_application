-- Jednostavan insert test podataka
-- Hibernate će kreirati tabele automatski

INSERT INTO race (id, name, distance) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Zagreb Marathon', 'Marathon'),
('550e8400-e29b-41d4-a716-446655440002', 'Plitvice Trail Run', 'HalfMarathon'),
('550e8400-e29b-41d4-a716-446655440003', 'Medvednica Challenge', 'TenK'),
('550e8400-e29b-41d4-a716-446655440004', 'Dalmatian Coast Run', 'HalfMarathon'),
('550e8400-e29b-41d4-a716-446655440005', 'Velebit Ultra Trail', 'Marathon'),
('550e8400-e29b-41d4-a716-446655440006', 'Park Run Zagreb', 'FiveK');

INSERT INTO application (id, race_id, first_name, last_name, club) VALUES
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440001', 'Marko', 'Marković', 'AK Zagreb'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440001', 'Petra', 'Petrić', 'TK Medvedgrad'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440002', 'Ivo', 'Ivić', 'TK Rijeka'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440002', 'Marija', 'Horvat', 'AK Zagreb'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440003', 'Sandra', 'Sandrić', 'AK Osijek'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440004', 'Kristina', 'Kristić', 'AK Pula'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440005', 'Mateo', 'Matić', 'TK Senj'),
(gen_random_uuid(), '550e8400-e29b-41d4-a716-446655440006', 'Ana', 'Anić', 'AK Split');

SELECT 'Test data inserted!' as status;
SELECT COUNT(*) FROM race;
SELECT COUNT(*) FROM application;