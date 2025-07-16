package com.thehotel.services;

public class HotelService {


    public HotelService(){};


    public String getGeneralInfo(){
        return String.format(
                    "Nome: %s%n" +
                    "Endereço: %s%n" +
                    "Classificação: %s%n" +
                    "Descrição: %n%s%n%n" +
                    "Comodidades: %n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n" +
                    "-> %s%n%n" +
                    "Horário de Check-in e Check-out: %n" +
                    "-> Check-in : %s%n" +
                    "-> Check-out : %s%n%n" +
                    "Política de Cancelamento: %n%s%n%n" +
                    "Contatos: %n" +
                    "-> Telefone: %s%n" +
                    "-> Email: %s%n",
                    "Hotel InfoUÉvora",
                    "R. Romão Ramalho 59, 7002-554 Évora",
                    "***** Estrelas",
                """
                        Situado no coração da histórica cidade de Évora, o hotel InfoUÉvora é o destino ideal para quem procura conforto e sofisticação.
                        Com uma localização privilegiada, este hotel combina a autenticidade do património cultural alentejano com o luxo e modernidade das instalações.\s
                        Desfrute de quartos elegantemente decorados, um serviço de excelência e uma ampla gama de comodidades que garantem uma estadia memorável, \
                        seja para negócios ou lazer.
                        Relaxe no nosso spa exclusivo e piscina exterior aquecida, saboreie pratos típicos no restaurante gourmet e contemple a vista deslumbrante sobre \
                        a cidade a partir do nosso rooftop.""",
                    "Piscina exterior aquecida",
                    "Rooftop",
                    "Spa e centro de bem-estar",
                    "Ginásio totalmente equipado",
                    "Restaurante gourmet com pratos regionais",
                    "Serviço de quarto 24 horas",
                    "Wi-fi gratuito em todas as áreas",
                    "Estacionamento gratuito",
                    "a partir das 15h00",
                    "até às 12h00",
                    "Cancelamento gratuito até 24 horas antes do check-in",
                    "+351 266 123 456",
                    "reservas@hotel-infouevora.pt"
        );
    }

}
