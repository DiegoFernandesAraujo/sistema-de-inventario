create database IF NOT EXISTS inventario_uepb;

use inventario_uepb;

alter table acervo_estante CHANGE cod_barras cod_barras varchar (250) NOT NULL;
alter table acervo_estante CHANGE obs obs varchar (250) NOT NULL;

show databases;
show tables;


select * from acervo_estante;
select * from acervo_siabi;
delete from acervo_siabi;

describe acervo_estante;

                

SELECT DISTINCT acervo_estante.seq, tombo, titulo, autor, localizacao
                FROM acervo_estante, acervo_siabi
                WHERE (cod_barras IS NOT NULL) AND (tombo IS NOT NULL);
                
#Seleciona não cadastrados                
SELECT seq, cod_barras FROM acervo_estante
WHERE cod_barras NOT IN (SELECT tombo FROM acervo_siabi);

   SELECT DISTINCT acervo_estante.seq, 
   IF (ac1.tombo = cod_barras, ac1.tombo, cod_barras) AS tombo,  
   IF (ac1.tombo = cod_barras, ac1.titulo, '') AS titulo, 
   IF (ac1.tombo = cod_barras, ac1.autor, '') AS autor, 
   IF (ac1.tombo = cod_barras, ac1.localizacao, '') AS localizacao
   FROM (acervo_estante, (SELECT tombo, titulo, autor, localizacao FROM acervo_siabi) AS ac1) ORDER BY acervo_estante.seq;

use inventario_uepb;

drop table acervo_estante;
drop table acervo_siabi;

drop database inventario_uepb;

use inventario_uepb;

CREATE TABLE IF NOT EXISTS acervo_estante (
	seq int not null,
	cod_barras varchar (25) NOT NULL,
    verificar varchar (5) NOT NULL,
    obs varchar (30) NOT NULL,
	primary key (seq)
);

   CREATE TABLE IF NOT EXISTS acervo_siabi (
	
	`seq` integer not null,
   `patrimonio` varchar(45) null,
   `tombo` varchar(25) null,
   `localizacao` varchar(45) null,
   `autor` varchar(255) null,
   `titulo` varchar(255) null,
   `edicao` varchar(45) null,
   `ano` varchar(45) null,
   `volume` varchar(45) null,
   `tomo` varchar(45) null,
   `valor` varchar(25) null,
   `nota_fiscal` varchar(45) null,
   `empenho` varchar(45) null,
   `rb` varchar(25) null,
   `situacao` varchar(45) null,
   primary key (`seq`)
   );
   
   select * from acervo_estante;
   insert into acervo_estante (seq, cod_barras, verificar, obs) values (12, '010510', 'nao', '-');
   #insert into acervo_estante (seq, cod_barras) values (10, 010519);
   
   SELECT cod_barras, localizacao
   FROM (SELECT tombo as cod_barras, localizacao as localizacao FROM acervo_siabi 
   where cod_barras in (SELECT tombo FROM acervo_siabi)) as teste;
   
   
   SELECT cod_barras, localizacao
   FROM (SELECT tombo as cod_barras, localizacao as localizacao FROM 
   (SELECT tombo, localizacao FROM acervo_siabi), acervo_estante
   where tombo = cod_barras) as teste;
   
   
   SELECT cod_barras, localizacao from (SELECT tombo as cod_barras, localizacao
   FROM acervo_siabi, acervo_estante
   WHERE tombo = cod_barras) as abc;
   
   
   SELECT tombo as cod_barras, localizacao as localizacao FROM acervo_siabi 
   where cod_barras in (SELECT tombo FROM acervo_siabi);
   
   use inventario_uepb;
   
   #funcionando
   SELECT cod_barras AS tombo, localizacao FROM (SELECT tombo, localizacao
   FROM acervo_siabi) as tbs, acervo_estante;
  
  describe acervo_estante;
  
  #Este aqui mais ou menos!
   SELECT DISTINCT cod_barras, localizacao FROM (SELECT localizacao, cod_barras
   FROM acervo_siabi, acervo_estante WHERE ((acervo_estante.seq = 9) OR (acervo_estante.seq =  10))) AS ultimos;
   
   SELECT tombo, localizacao, cod_barras
   FROM acervo_siabi, acervo_estante WHERE ((acervo_estante.seq = 30) OR (acervo_estante.seq =  31));
   
   SELECT DISTINCT cod_barras, IF (tombo = cod_barras, localizacao, '') AS localizacao2 FROM (SELECT tombo, localizacao, cod_barras
   FROM acervo_siabi, acervo_estante WHERE ((acervo_estante.seq = 30) OR (acervo_estante.seq =  31))) AS ultimos;
   
   #---------------------
   select count(*) as tamanho
   from acervo_estante;
   select * from acervo_estante;
   
   select localizadosSE.cod_barras, locali
   from (SELECT cod_barras, localizacao as locali from acervo_siabi, acervo_estante 
   where cod_barras = tombo) as localizadosSE, acervo_estante ae
   where ((ae.cod_barras not in  (localizadosSE.cod_barras)) and seq = (select count(*) from acervo_estante as tamanho));
   
   #we, 120701, 120701, ghh
   SELECT tombo, localizacao
   from acervo_siabi
   where tombo in
   (Select cod_barras 
   from acervo_estante ae, 
   (select count(*) as tamanho from acervo_estante) as total
   where ae.seq = tamanho - 3 or ae.seq = tamanho - 2
   or ae.seq = tamanho - 1 or ae.seq = tamanho);

   #####
   (Select cod_barras as cb 
   from acervo_estante ae, 
   (select count(*) as tamanho from acervo_estante) as total
   where ae.seq = tamanho - 3 or ae.seq = tamanho - 2
   or ae.seq = tamanho - 1 or ae.seq = tamanho) ultimos
	where 1 = 1 ;
   
   
   #SELECT DISTINCT
    #    cod_barras,
     #       IF(tombo = cod_barras, localizacao, '') AS localizacao2
   
   
   select * from acervo_estante;
   #ok++++++++++++++++++
   Select cod_barras 
   from acervo_estante ae, 
   (select count(*) as tamanho from acervo_estante) as total
   where ae.seq = tamanho - 3 or ae.seq = tamanho - 2
   or ae.seq = tamanho - 1 or ae.seq = tamanho;
   #====================+++
   
   
   #---------------------
   
SELECT 
    cod_barras, localizacao2
FROM
    (SELECT DISTINCT
        cod_barras,
            IF(tombo = cod_barras, localizacao, '') AS localizacao2
    FROM
        (SELECT 
        tombo, localizacao, cod_barras
    FROM
        acervo_siabi, acervo_estante
    WHERE
        ((acervo_estante.seq = 31)
            OR (acervo_estante.seq = 32))) AS ultimos
    WHERE
        (((ultimos.cod_barras = tombo)
            AND (ultimos.localizacao <> ''))
            OR ((cod_barras <> tombo)
            AND (ultimos.localizacao = '')))) AS tabela;

SELECT cod_barras, localizacao FROM (SELECT DISTINCT cod_barras, IF (tombo = cod_barras, localizacao, '') AS localizacao FROM (SELECT tombo, localizacao, cod_barras
   FROM acervo_siabi, acervo_estante WHERE ((acervo_estante.seq = 14) OR (acervo_estante.seq =  15))) AS ultimos
   WHERE (((ultimos.cod_barras = tombo) AND (ultimos.localizacao <> ''))
            OR ((cod_barras <> tombo) AND (ultimos.localizacao = '')))) AS tabela; 
            
SELECT COUNT(*) from acervo_estante;
            
#FUNCIONOU, MAS ATRAVÉS DE GAMBIARRA            

            
SELECT cod_barras, localizacao FROM (
   SELECT cod_barras, localizacao FROM (SELECT DISTINCT cod_barras, IF (tombo = cod_barras, localizacao, '') AS localizacao 
		 FROM (SELECT tombo, localizacao, cod_barras
			   FROM acervo_siabi, acervo_estante 
			   WHERE ((acervo_estante.seq = 41) OR (acervo_estante.seq =  42))) AS ultimos
   WHERE (((ultimos.cod_barras = tombo) AND (ultimos.localizacao <> ''))
            OR ((cod_barras <> tombo) AND (ultimos.localizacao = '')))) AS tabela ORDER BY localizacao DESC LIMIT 2) as final ORDER BY localizacao;            
   
   
   LIMIT 2;
   
   
   #Este!
    
   SELECT cod_barras AS Tombo, IF (tombo = cod_barras, localizacao, '') AS Localização FROM (SELECT tombo, localizacao
   FROM acervo_siabi) as tbs, acervo_estante;
   
   select * from acervo_estante;
	select * from acervo_siabi;
    
    SELECT cod_barras FROM (SELECT tombo FROM acervo_siabi) AS tbs, acervo_estante
    WHERE 063200 = tombo;
    
    SELECT tombo FROM acervo_siabi WHERE 500 = tombo;
    
    

SELECT * FROM (SELECT cod_barras FROM acervo_estante
                WHERE ((seq = 9)
                OR (seq =  8) 
                OR (seq = 10) 
                OR (seq =  11))) AS vizinhos, acervo_siabi
                WHERE cod_barras = tombo;
                
                SET @ant1 = (SELECT cod_barras FROM acervo_estante WHERE (seq =  8));
                SET @ant2 = (SELECT cod_barras FROM acervo_estante WHERE (seq =  7));
                
                SET @post2 = (SELECT cod_barras FROM acervo_estante WHERE (seq =  10));
               
                SET @post1 = (SELECT cod_barras FROM acervo_estante WHERE (seq =  11));
               
                SELECT * FROM @ant1 INNER JOIN @ant2;
                
                