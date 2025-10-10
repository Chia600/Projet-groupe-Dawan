USE booktracker;

INSERT INTO author (firstname,lastname,version) VALUES
('Stephen','King',0),
('TJ','Klune',0),
('Sarah J.','Maas',0),
('Bruce','Eckel',0),
('Diego','Rodrigues',0);

INSERT INTO category (genre,version) VALUES 
('Horror', 0),
('Computer', 0),
('Fantasy', 0);

INSERT INTO book (cover,description,page_number,publication_date,title,author_id,category_id,version) VALUES
('http://books.google.com/books/content?id=ZejEDwAAQBAJ&printsec=frontcover&img=1', 'Ténèbres. Puis douleur et brume... Quand il reprend conscience, c\'est pour sentir l\'haleine empuantie de la femme qui le ramène à la vie. Au premier regard, Paul Sheldon comprend qu\'il est dans un sacré pétrin.',370,'2017-02-28','Misery',1,1,0),
('https://storage.googleapis.com/c80429fc-2c82-45e8-be63-a7ff5b0510f4/products/1492073/07bff479-ea73-4188-8679-1a77cd7f0b5f_zoom.jpg', 'Bienvenue dans notre salon de thé "La Traversée de Charon". Le thé y est chaud, les scones bien frais et les morts, juste de passage. Wallace Price n\'est pas ce que l\'on peut appeler "un personnage sympathique" ... loin de là même.',403,'2022-12-08','Sous la porte qui chuchote',2,3,0),
('https://cdn.kobo.com/book-images/bf871832-1b7d-44d4-b7b3-09a5688b0d05/353/569/90/False/l-ere-de-la-supernova-2.jpg', 'Après cinq cents millions d\'existence, une étoile inconnue de la constellation du Cocher finit ses jours en une spectaculaire explosion d\'énergie',464,'2003','L\'ère de la supernova',NULL,NULL,0),
('https://m.media-amazon.com/images/I/61xSg4F0vWL._SL1491_.jpg', 'Bienvenue dans "FONCTIONS AVANCÉES DE KALI LINUX - Édition 2024", le guide ultime pour maîtriser les fonctions avancées de la distribution de cybersécurité la plus puissante du marché. Ce livre, écrit par Diego Rodrigues, expert international en intelligence de marché et en innovation avec plus de 180 titres publiés en six langues, offre une immersion profonde dans les outils et techniques qui font de Kali Linux le choix numéro un des professionnels de la cybersécurité dans le monde entier.',183,'2024-10-30','APPRENEZ GO Édition 2024',5,2,0);


INSERT INTO user (firstname,lastname,email,password,picture,subscription_date,username,version,is_subscribed) VALUES
('Donald', 'Duck', 'dduck@duckburg.wd', 'mypassword', NULL, '2025-10-06', 'powerDuck', 0, true);

INSERT INTO review (creation_date,rating,review,user_id,book_id) VALUES
('2025-10-06',4, NULL, 1, 1);

INSERT INTO user_books (user_user_id,books_book_id) VALUES
(1, 3);


