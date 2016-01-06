create database IF NOT EXISTS inventario_uepb;

show databases;
show tables;

select * from acervo_estante;
select * from acervo_siabi;
delete from acervo_siabi;


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