create database IF NOT EXISTS inventario_uepb;

#describe inventario_uepb;

use inventario_uepb;

show databases;
show tables from inventario_uepb;

show columns from acervo_estante;
show fields from acervo_estante;
select * from acervo_estante;
describe acervo_estante;
drop table acervo_estante;

delete from acervo_estante;

show columns from acervo_siabi;
show fields from acervo_siabi;
select * from acervo_siabi;
describe acervo_siabi;

set @tamanho := (Select count(*) ultimo from acervo_estante);
select @tamanho; 
show variables;



set @tam = 10;
select @tam; 

DELETE FROM acervo_estante WHERE 
(SELECT seq FROM (SELECT COUNT(*) ultimo FROM acervo_estante) AS tamanho WHERE seq = ultimo);

SELECT *,@var := @var + 1 AS sequencia FROM teste t1 ORDER BY nome;


ALTER TABLE acervo_estante AUTO_INCREMENT = (@tamanho-1);
ALTER TABLE acervo_estante AUTO_INCREMENT = @tam;

Insert into acervo_estante (seq, cod_barras, verificar, obs) values (7, 'diego', 'sim', 'teste');

/*ALTER TABLE acervo_estante CHANGE obs obs varchar(45) not null;
ALTER TABLE acervo_estante CHANGE obs obs varchar(250) not null;
ALTER TABLE acervo_estante CHANGE verf verificar varchar(5) not null;*/

ALTER TABLE acervo_estante CHANGE seq seq int not null;

CREATE TABLE acervo_estante (
	seq INT NOT NULL,
	cod_barras varchar (25) NOT NULL,
    verificar varchar (5) NOT NULL,
    obs varchar (30) NOT NULL,
	PRIMARY KEY (seq)
);

/*CREATE TABLE acervo_siabi (
	
	`seq` integer AUTO_INCREMENT,
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
   PRIMARY KEY (`seq`)
   );*/
   
   CREATE TABLE acervo_siabi (
	
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
   PRIMARY KEY (`seq`)
   );