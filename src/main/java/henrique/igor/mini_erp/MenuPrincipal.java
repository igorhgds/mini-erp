package henrique.igor.mini_erp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import henrique.igor.mini_erp.dao.ParceiroDAO;
import henrique.igor.mini_erp.dao.PedidoDAO;
import henrique.igor.mini_erp.dao.ProdutoDAO;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Parceiro;
import henrique.igor.mini_erp.model.Pedido;
import henrique.igor.mini_erp.model.Produto;
import henrique.igor.mini_erp.service.PedidoService;

public class MenuPrincipal {
	
	public static void main(String[] args) {
		PedidoDAO pedidoDao = new PedidoDAO();
		ProdutoDAO produtoDao = new ProdutoDAO();
		ParceiroDAO parceiroDao = new ParceiroDAO();
		PedidoService pedidoService = new PedidoService(pedidoDao, produtoDao);
		
		Scanner scanner = new Scanner(System.in);
		int opcao = -1;
		
		while(opcao != 0) {
			System.out.println("\n===================================");
            System.out.println("      MINI-ERP JAVA PURO V1.0      ");
            System.out.println("===================================");
            System.out.println("[1] - Cadastrar Novo Parceiro");
            System.out.println("[2] - Cadastrar Novo Produto");
            System.out.println("[3] - Lançar um Pedido de Venda");
            System.out.println("[4] - Consultar Pedido por ID");
            System.out.println("[5] - Consultar Parceiro por ID");
            System.out.println("[6] - Consultar Produto por ID");
            System.out.println("[0] - Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
            case 1: //CADASTRO DE PARCEIRO
                System.out.println("\n--- CADASTRO DE PARCEIRO ---");
                
                System.out.println("Nome do Parceiro: ");
                String nome = scanner.nextLine();
                
                System.out.println("Tipo Pessoa (F - Física / J - Jurídica): ");
                String tipPessoa = scanner.nextLine();
                
                System.out.println("Ativo (S - Sim / N - Não): ");
                String ativo = scanner.nextLine();
                
                Parceiro novoParceiro = new Parceiro();
                novoParceiro.setNomeParc(nome);
                novoParceiro.setTipPessoa(tipPessoa);
                novoParceiro.setAtivo(ativo);
                
                parceiroDao.salvar(novoParceiro);
                break;
                
            case 2: //CADASTRO DE PRODUTO
                System.out.println("\n--- CADASTRO DE PRODUTO ---");
                
                System.out.print("Descrição do Produto: ");
                String descr = scanner.nextLine();
                
                System.out.print("Valor de Venda (Ex: 9.99): ");
                BigDecimal vlrVenda = scanner.nextBigDecimal();
                scanner.nextLine();
                
                System.out.print("Estoque Inicial: ");
                BigDecimal estoque = scanner.nextBigDecimal(); 
                scanner.nextLine();
                
                // Montando o DTO
                Produto novoProduto = new Produto();
                novoProduto.setDescrProd(descr);
                novoProduto.setVlrVenda(vlrVenda);
                novoProduto.setEstoque(estoque);
                
                produtoDao.salvar(novoProduto);
                break;
                
            case 3: //NOVO PEDIDO
            	System.out.println("\n--- INICIANDO NOVO PEDIDO ---");
                
                // 1. Validar e injetar o Parceiro
                System.out.print("Digite o ID (CODPARC) do Parceiro: ");
                int idParceiro = scanner.nextInt();
                scanner.nextLine();
                
                Parceiro parceiro = parceiroDao.buscarParceiro(idParceiro);
                
                if (parceiro == null || !"S".equals(parceiro.getAtivo())) {
                    System.out.println("Erro: Parceiro não encontrado ou inativo. Abortando pedido.");
                    break;
                }

                // 2. Montar o Cabeçalho (TGFCAB)
                Pedido novoPedido = new Pedido();
                novoPedido.setParceiro(parceiro);
                novoPedido.setDtNeg(new java.sql.Date(System.currentTimeMillis()));
                novoPedido.setStatus("P");
                
                // Simulando a digitação do Número da Nota Fiscal (documento físico)
                System.out.print("Digite o Número da Nota (NUMNOTA): ");
                int numNota = scanner.nextInt();
                novoPedido.setNumNota(numNota);

                // 3. Loop para preencher os Itens (TGFITE)
                List<ItemPedido> listaItens = new ArrayList<>();
                int sequencia = 1;
                BigDecimal totalDaNota = BigDecimal.ZERO;

                System.out.println("\n--- Adicionando Produtos ---");
                while (true) {
                    System.out.print("\nDigite o ID do Produto (ou 0 para fechar o pedido): ");
                    int idProduto = scanner.nextInt();
                    
                    if (idProduto == 0) break;

                    Produto produto = produtoDao.buscarProduto(idProduto);
                    
                    if (produto == null) {
                        System.out.println(">>> Erro: Produto " + idProduto + " não existe no cadastro!");
                        continue;
                    }
                    
                    System.out.println("Produto: " + produto.getDescrProd() + " | Preço Tabela: " + produto.getVlrVenda());
                    
                    System.out.print("Quantidade: ");
                    BigDecimal qtd = scanner.nextBigDecimal();
                    scanner.nextLine(); 
                    
                    System.out.print("Valor Unitário (Pressione Enter para usar " + produto.getVlrVenda() + "): ");
                    String inputVlr = scanner.nextLine().trim();
                    
                    BigDecimal vlrUnit;
                    if (inputVlr.isEmpty()) {
                        vlrUnit = produto.getVlrVenda();
                    } else {
                        vlrUnit = new BigDecimal(inputVlr.replace(",", "."));
                    }

                    // Montando a linha do Item
                    ItemPedido item = new ItemPedido();
                    item.setSequencia(sequencia++);
                    item.setProduto(produto);
                    item.setQtdNeg(qtd);
                    item.setVlrUnit(vlrUnit);

                    listaItens.add(item);
                    
                    totalDaNota = totalDaNota.add(item.getVlrTot());
                }

                if (listaItens.isEmpty()) {
                    System.out.println("\nOperação cancelada: Um pedido precisa ter pelo menos 1 item.");
                    break;
                }

                novoPedido.setItens(listaItens);
                novoPedido.setVlrNota(totalDaNota);

                try {
                    System.out.println("\nProcessando faturamento... Aguarde.");
                    pedidoService.realizarPedido(novoPedido);
                    System.out.println(">>> SUCESSO! Pedido faturado. NUNOTA Gerado: " + novoPedido.getNuNota());
                } catch (Exception e) {
                    System.err.println("\n>>> FALHA NO FATURAMENTO: " + e.getMessage());
                } break;
                
            case 4: //CONSULTA DE PEDIDO
                System.out.println("\n--- CONSULTA DE PEDIDO ---");
                System.out.print("Digite o ID (NUNOTA) do Pedido: ");
                int idBusca = scanner.nextInt();
                scanner.nextLine();
                
                Pedido pedido = pedidoDao.buscarPorId(idBusca);
                
                if (pedido != null) {
                    System.out.println("\n=== PEDIDO ENCONTRADO ===");
                    System.out.println("ID (NuNota): " + pedido.getNuNota());
                    System.out.println("Nº Nota: " + pedido.getNumNota());
                    System.out.println("Valor Total: " + pedido.getVlrNota());
                    System.out.println("ID do Parceiro: " + pedido.getParceiro().getCodParc());
                    
                    System.out.println("--- ITENS DO PEDIDO ---");
                    for (ItemPedido item : pedido.getItens()) {
                        System.out.println("Seq: " + item.getSequencia() + 
                                           " | Produto ID: " + item.getProduto().getCodProd() + 
                                           " | Qtd: " + item.getQtdNeg() + 
                                           " | Vlr. Unit: " + item.getVlrUnit() +
                                           " | Vlr. Tot: " + item.getVlrTot());
                    }
                } else {
                    System.out.println("Pedido não encontrado no banco de dados.");
                } break;
                
            case 5: //CONSULTA PARCEIRO
            	System.out.println("\n--- CONSULTA PARCEIRO ---");
            	System.out.println("Digite o ID (CODPARC) do Parceiro");
            	int codparc = scanner.nextInt();
            	scanner.nextLine();
            	
            	Parceiro buscaParceiro = parceiroDao.buscarParceiro(codparc);
            	
            	if(buscaParceiro != null) {
            		System.out.println("\n=== PARCEIRO ENCONTRADO ===");
            		System.out.println("ID (CODPARC): " + buscaParceiro.getCodParc());
            		System.out.println("Parceiro: " + buscaParceiro.getNomeParc());
            		System.out.println("Tipo Pessoa: " + buscaParceiro.getTipPessoa());
            		System.out.println("Ativo: " + buscaParceiro.getAtivo()); 		
            	}else {
                    System.out.println("Pedido não encontrado no banco de dados.");
                } break;
                
            case 6: //CONSULTA PRODUTO
            	System.out.println("\n--- CONSULTA PRODUTO ---");
            	System.out.println("Digite o ID (CODPROD) do Produto");
            	int codprod = scanner.nextInt();
            	scanner.nextLine();
            	
            	Produto buscaProduto = produtoDao.buscarProduto(codprod);
            	
            	if(buscaProduto != null) {
            		System.out.println("\n=== PARCEIRO ENCONTRADO ===");
            		System.out.println("ID (CODPROD): " + buscaProduto.getCodProd());
            		System.out.println("Descrição: " + buscaProduto.getDescrProd());
            		System.out.println("Vlr. Venda: " + buscaProduto.getVlrVenda());
            		System.out.println("Estoque: " + buscaProduto.getEstoque()); 		
            	}else {
                    System.out.println("Pedido não encontrado no banco de dados.");
                } break;
            	
            case 0: //SAIR DO PROGRAMA
                System.out.println("\nDesligando os motores... Até logo!");
                break;
                
            default:
                System.out.println("\nOpção inválida! Tente novamente.");
            }
		}
		
			scanner.close();
		}

}
