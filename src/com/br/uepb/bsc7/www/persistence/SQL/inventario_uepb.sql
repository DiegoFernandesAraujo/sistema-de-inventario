create database IF NOT EXISTS inventario_uepb;

#describe inventario_uepb;

use inventario_uepb;


show databases;
show columns from acervo_estante;
show fields from acervo_estante;
select * from acervo_estante;

show columns from acervo_siabi;
show fields from acervo_siabi;
select * from acervo_siabi;


describe acervo_estante;
drop table acervo_estante;
describe acervo_siabi;
drop table acervo_siabi;

CREATE TABLE acervo_estante (
	seq INT AUTO_INCREMENT,
	cod_barras varchar (25) NOT NULL,
    verf varchar (5) NOT NULL,
    obs varchar (30) NOT NULL,
	PRIMARY KEY (seq)
);

CREATE TABLE acervo_siabi (
	
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
   );