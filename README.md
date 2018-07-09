# Software para Treinamento e Exportação de Redes Neurais Artificiais Arquitetura Perceptron Multicamadas<b>
Software parte do trabalho de conclusão de curso do curso de Engenharia de Controle e Automação, CEFET-MG<br>
"Sistema de Controle Sensorless de um Motor de Indução Utlizando Estimador Neural"<p>

Este é um software livre, sem fins lucrativos que visa facilitar o desenvolvimento, uso e validação de redes neurais artificiais. Criado como uma ferramenta simplificar e agilizar a implementação de redes neurais artificiais em sistemas embarcados e em softwares de simulação que possam utilizar funções em C. O sistema também possui como objetivo estimular o uso de RNAs por pessoas com pouco contato na área. A opção de exportação em arquivo .c é muito útil como ferramenta didática na apresentação dos conceitos de redes neurais (atualmente redes pmc são suportadas).


Para utilizar o aplicativo é necessário um conjunto com os dados de treinamento e um arquivo de configurações.
O arquivo de configurações deverá estar salvo como .properties e ele irá descrever quais colunas do banco de treinamento são
relativas a dados de entrada e quais são de saída. Um exemplo seria:

##
\# Arquivo properties:
<b>
    <br>in=0,1
    <br>out=2
</b><p>
##
\# Arquivo Dataset:
<b><p>     0 0 0
<b><p>     0 1 1
<b><p>     1 0 1
<b><p>     1 1 0
</b>
<p>
##
por:        Cláudio Ferreira Carneiro, Bacharelando em Engenharia de Controle e Automação<p>
Orientador: Carlos Henrique Silva de Vasconcelos http://lattes.cnpq.br/4476642168253767
