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
	
SELECT person.name, company.name FROM person
INNER JOIN company
ON person.company_id = company.id AND company.id != 5


SELECT company.name, COUNT(*) 
FROM person, company
WHERE person.company_id = company.id
GROUP BY  person.company_id, company.name
HAVING count(*) = 
(
	SELECT COUNT(person.name) 
	FROM person 
	GROUP BY person.company_id  
	ORDER BY COUNT(person.name) DESC
	LIMIT 1
)


DROP TABLE person;
DROP TABLE company;
