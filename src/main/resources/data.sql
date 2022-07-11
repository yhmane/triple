INSERT INTO review (review_id, user_id, content, place_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', '3ede0ef2-92b7-4817-a5f3-0c575361f745', '좋아요!!',
    '2e4baf1c-5acb-4efb-a1af-eddada31b00f', now(), now());
INSERT INTO review_photo (review_id, attached_photo_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', 'e4d1a64e-a531-46de-88d0-ff0ed70c0bb8', now(), now());
INSERT INTO review_photo (review_id, attached_photo_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', 'afb0cef2-851d-4a50-bb07-9cc15cbdc332', now(), now());

INSERT INTO point (user_id, points, created_at, updated_at)
    VALUES ('3ede0ef2-92b7-4817-a5f3-0c575361f745', '3', now(), now());
INSERT INTO point_history (review_id, points, action_type, point_type, increase_type, user_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', 1, 'ADD', 'REVIEW', 'PLUS', '3ede0ef2-92b7-4817-a5f3-0c575361f799',  now(), now());
INSERT INTO point_history (review_id, points, action_type, point_type, increase_type, user_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', 2, 'ADD', 'PHOTO', 'PLUS','3ede0ef2-92b7-4817-a5f3-0c575361f799',  now(), now());
INSERT INTO point_history (review_id, points, action_type, point_type, increase_type, user_id, created_at, updated_at)
    VALUES ('240a0658-dc5f-4878-9381-ebb7b2667772', 3, 'ADD', 'BONUS', 'PLUS','3ede0ef2-92b7-4817-a5f3-0c575361f799',  now(), now());