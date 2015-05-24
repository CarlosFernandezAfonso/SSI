# SSI

Correspondecia de exercicios a directorias

Exercicio 1.1              >     ./SSI_RC4
Exercicio 1.2              >     ./Aula 1.2
Exercicio 2                >     ./Aula 2
Exercicio 3.1, 3.2, 4.1    >     ./Aula 4.1
Exercicio 4.2              >     ./Aula 4.2
Exercicio 5                >     ./Aula 5 
Exercicio 6                >     ./ServidorApache





openssl verify -CAfile ./ca/signing-ca-chain.pem ./certs/carlos.crt

openssl verify -CAfile ./ca/signing-ca-chain.pem ./certs/tiagooutlook.crt ./certs/carlos.crt ./certs/tiago.crt