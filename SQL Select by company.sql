CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

INSERT INTO company (id, name) VALUES (1, 'ООО ПАН'),(2,'ОАО Звезда'),(3,'ООО ТВЕ'),(4,'ООО ЕВА'),(5,'ОАО ЦЦК');
INSERT INTO person (id, name, company_id) VALUES (1, 'Иван', 4),(2,'Александр',4),(3,'Николай',5),
	(4,'Никита',1),(5,'Роман',4),(6,'Федор',5),(7,'Дмитрий',3),(8,'Евгений',4),(9,'Петр',3),(10,'Светлана',5);
	
SELECT * FROM person WHERE company_id != 5;
SELECT p.name,c.name FROM person as p, company as c WHERE p.company_id = c.id;



SELECT company.name, rsl_count.count FROM company, 
	(
		SELECT company.id, company.name, MAX(rsl_count_for_max.count) FROM company,
			(
				SELECT company.id, company.name, person.company_id, COUNT(person.name) FROM person, company
				WHERE person.company_id = company.id 
				GROUP BY company.name, person.company_id, company.id
			) 	
		AS rsl_count_for_max
		GROUP BY company.id, company.name
	) 
AS rsl_max,
	
	(
		SELECT company.id, company.name, person.company_id, COUNT(person.name) FROM person, company
		WHERE person.company_id = company.id 
		GROUP BY company.name, person.company_id, company.id
	) 
AS rsl_count
WHERE rsl_max.max = rsl_count.count AND company.id = rsl_count.id AND company.id = rsl_max.id

 
DROP TABLE person;
DROP TABLE company;
