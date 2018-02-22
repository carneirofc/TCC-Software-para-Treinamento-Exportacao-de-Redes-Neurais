-- Instruções para o preenchimento do arquivo de properties. --

 
colunasEntrada -> Quais das colunas presentes no arquivo de treinamento deverão ser tratadas como
valores de entrada. A mesma coluna pode ser usada tando como entrada como saída. A contagem das
colunas é feita a partir de 0 até o número em questão. Não deve ser informado um número maior que o 
número de colunas (contagem a partir de zero).
Ex:

colunasEntrada=0,4,2,1
A primeira (0),segunda(1), quinta(4) e terceira(2) colunas do arquivo de .txt serão de entrada.

----------------------------------
colunasSaida -> Quais das colunas presentes no arquivo de treinamento deverão ser tratadas como
valores de saída. A mesma coluna pode ser usada tando como entrada como saída. A contagem das
colunas é feita a partir de 0 até o número em questão. Não deve ser informado um número maior que o 
número de colunas (contagem a partir de zero).

Ex:

colunasSaida=0,4,2
A primeira (0), quinta(4) e terceira(2) colunas do arquivo de .txt serão de saída.
