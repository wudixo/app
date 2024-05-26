INSERT
INTO public.persons(id, forename, surname, username, email, phone, date_of_birth, password, age, role)
VALUES  ('07df9d6a-79cc-11ec-90d6-0242ac120004', 'tester', 'one', 'tester.one', 'tester.one@iaido.com', 07765477567, now(), 'secret', 45, 'GUEST'),
        ('19526444-7b05-11ec-90d6-0242ac120005', 'tester', 'two', 'tester.two', 'tester.two@iaido.com', 07765477567, now(), '$2a$10$n44MCfrQb9zfDhqv4cgna.hPMAbVPxKh7Bq.pJ0.PpRWQA/l7Mb9O', 44, 'GUEST'),



        ('19526444-7b05-11ec-90d6-0242ac120435', 'tester', 'two', 'guest.person', 'tester.two@iaido.com', 07765477567, now(), '$2a$10$n44MCfrQb9zfDhqv4cgna.hPMAbVPxKh7Bq.pJ0.PpRWQA/l7Mb9O', 41, 'GUEST'),
        ('19526444-7b05-11ec-90d6-0242ac120653', 'tester', 'two', 'admin.person', 'tester.two@iaido.com', 07765477567, now(), '$2a$10$n44MCfrQb9zfDhqv4cgna.hPMAbVPxKh7Bq.pJ0.PpRWQA/l7Mb9O', 46, 'ADMIN');
