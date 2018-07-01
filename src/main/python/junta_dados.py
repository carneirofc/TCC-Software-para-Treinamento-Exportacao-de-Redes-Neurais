import sys

try:
    print(str(sys.argv[-1]))

    if len(sys.argv) < 3:
        raise ValueError('Falta argumento...')


    if sys.argv[0] in str(sys.argv[-1]):
        raise ValueError('Altere o último arquivo')

    print('Verifique se os arquivos possuem somente as colunas desejadas. Pode ser que a primeira coluna marcando tempo seja adicinoada em todos os outros (como no PSCAD)')

    l_conf = []
    out = open(sys.argv[-1], 'w+')

    print('Arquivos a serem concatenados: \n')

    for f_nome in sys.argv[1:-1]:
        print(f_nome + '\t\n')
        with open(f_nome, 'r') as f:
            l = [line.rstrip('\n') for line in f]
            l_conf.append(l)

    linhas_num = []
    i = 0
    for l in l_conf:
        linhas_num.append(len(l))
        print('Número de linhas no arquivo {}: {}'.format(i, len(l)))
        i += 1

    min_linha = min(linhas_num)
    max_linha = max(linhas_num)

    print('Arquivos {} min_linhas={} max_linhas={}\n'.format(
        len(l_conf), max_linha, min_linha))

    for i in range(0, min_linha):
        s_aux = ''
        for l in l_conf:
            s_aux += ' {}'.format(l[i])
        out.write(s_aux + '\n')

    out.close()

except Exception as ex:
    print('Algum erro ocorreu. Verifique se os devidos parâmetros foram passados.')
    print('Último parâmetro é o arquivo a ser gerado.')
    print(ex)
pass
