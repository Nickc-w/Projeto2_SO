# 📘 Trabalho 2 – Sistemas Operacionais
## Problema do Produtor–Consumidor (Oferta e Procura)

---

## 📌 Descrição Geral do Projeto

Este projeto implementa uma solução para o clássico problema do **Produtor–Consumidor**, estudado na disciplina de **Sistemas Operacionais**, utilizando **threads**, **semáforos** e **mecanismos de exclusão mútua** em Java.

O objetivo é demonstrar como **threads concorrentes** podem acessar um **recurso compartilhado** de forma segura, evitando conflitos, inconsistências e problemas como **condições de corrida**.

No sistema desenvolvido, existem duas threads principais:

- **Produtor**, responsável por gerar dados e inseri-los em um buffer.
- **Consumidor**, responsável por remover dados desse buffer.

O buffer possui capacidade máxima de **7 posições**, e o acesso a ele é controlado para garantir que:

- O produtor não insira dados quando o buffer estiver cheio.
- O consumidor não remova dados quando o buffer estiver vazio.

---

## 🧠 Lógica de Funcionamento

O buffer funciona como um **recurso compartilhado**, acessado simultaneamente por mais de uma thread. Para garantir a sincronização correta, foram utilizados:

- **Semáforos**, para controlar:
  - A quantidade de espaços disponíveis no buffer.
  - A quantidade de itens disponíveis para consumo.
- **Mutex (ReentrantLock)**, para garantir exclusão mútua, permitindo que apenas uma thread por vez acesse o buffer.

Cada item produzido ocupa uma posição no buffer, e cada item consumido libera uma posição.

O produtor pode produzir até **15 itens**, enquanto o consumidor pode consumir até **12 itens**, sempre respeitando os limites do buffer.

---

## 🧵 Threads Utilizadas

### 🔹 Thread Produtor
A thread produtora simula a geração de dados.  
Ela só pode inserir um item no buffer se houver espaço disponível.  
Caso o buffer esteja cheio, o produtor é bloqueado até que o consumidor libere espaço.

### 🔹 Thread Consumidor
A thread consumidora remove dados do buffer.  
Ela só pode consumir um item se houver itens disponíveis.  
Caso o buffer esteja vazio, o consumidor é bloqueado até que o produtor produza novos itens.

---

## 🔐 Controle de Sincronização

### Semáforos Utilizados

- **Semáforo de espaços disponíveis**
  - Inicializado com valor **7**, representando os espaços livres do buffer.
  - Controla se o produtor pode ou não inserir novos itens.

- **Semáforo de itens disponíveis**
  - Inicializado com valor **0**, pois o buffer começa vazio.
  - Controla se o consumidor pode ou não remover itens.

Esses semáforos garantem que as threads sejam **bloqueadas automaticamente** quando não podem executar suas operações.

### Mutex (ReentrantLock)

O mutex é utilizado para garantir **exclusão mútua** no acesso ao buffer.  
Isso impede que produtor e consumidor modifiquem o buffer simultaneamente, evitando inconsistências e condições de corrida.

---

## 📄 Registro de Execução (Log)

Todas as operações realizadas pelas threads são registradas em um arquivo texto, contendo:

- Inserções realizadas pelo produtor.
- Remoções realizadas pelo consumidor.
- Quantidade de espaços disponíveis no buffer após cada operação.

Esse log permite acompanhar o comportamento concorrente das threads ao longo da execução do programa.

---

## 🎓 Paralelo com os Conteúdos de Sistemas Operacionais

Este trabalho se relaciona diretamente com os conceitos estudados em sala de aula:

- As classes **Produtor** e **Consumidor** representam **threads de usuário**, que executam concorrentemente dentro de um mesmo processo.
- O **buffer** representa uma **região crítica**, pois é um recurso compartilhado entre múltiplas threads.
- O uso de **semáforos** reflete os mecanismos clássicos de sincronização utilizados pelo sistema operacional.
- O bloqueio de threads quando o buffer está cheio ou vazio ilustra os conceitos de **espera e escalonamento**.
- O uso do **mutex (ReentrantLock)** representa o conceito de **exclusão mútua**, fundamental para evitar condições de corrida.

Dessa forma, o projeto demonstra na prática como o sistema operacional coordena múltiplas threads para garantir uma execução correta, segura e eficiente.

---

## ✅ Conclusão

O projeto permite compreender, de forma prática, como ocorre a sincronização entre threads concorrentes e como os mecanismos de Sistemas Operacionais são aplicados para resolver problemas clássicos como o Produtor–Consumidor, garantindo integridade dos dados e correto compartilhamento de recursos.
