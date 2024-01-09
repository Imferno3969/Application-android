#include <iostream>
#include <winsock2.h>
#include "Bibliotheque.h" // Make sure to include the correct path
#include <conio.h>

class Serveur_TCP
{
private:
    SOCKET TCP_Socket;
    SOCKET TCP_Client;
    SOCKADDR_IN adresse;
    int Adresse;
    int type_communication;
    int protocole;
    DWORD DwFlag;
    WSADATA VersionInfos;
    WORD Version_Requise;
    int erreur_version;

public:
    Serveur_TCP()
    {
        // Constructor
        erreur_version = WSAStartup(MAKEWORD(2, 2), &VersionInfos);
        if (erreur_version != 0)
        {
            std::cerr << "Erreur lors de l'initialisation de WinSock" << std::endl;
            // Handle error appropriately
        }
    }

    ~Serveur_TCP()
    {
        // Destructor
        closesocket(TCP_Socket);
        WSACleanup();
    }

    int NumVersion()
    {
        return erreur_version;
    }

    int SocketCreation()
    {
        TCP_Socket = socket(AF_INET, SOCK_STREAM, 0);
        if (TCP_Socket == INVALID_SOCKET)
        {
            std::cerr << "Erreur lors de la création du socket" << std::endl;
            // Handle error appropriately
            return -1;
        }
        return 0;
    }

    int SocketConnexion()
    {
        adresse.sin_family = AF_INET;
        adresse.sin_addr.s_addr = inet_addr("127.0.0.1");
        adresse.sin_port = htons(23);

        int result = connect(TCP_Socket, (struct sockaddr *)&adresse, sizeof(adresse));
        if (result == SOCKET_ERROR)
        {
            std::cerr << "Erreur lors de la connexion au serveur" << std::endl;
            // Handle error appropriately
            return -1;
        }
        return 0;
    }

    int SocketEcoute()
    {
        adresse.sin_family = AF_INET;
        adresse.sin_addr.s_addr = INADDR_ANY;
        adresse.sin_port = htons(23);

        int result = bind(TCP_Socket, (struct sockaddr *)&adresse, sizeof(adresse));
        if (result == SOCKET_ERROR)
        {
            std::cerr << "Erreur lors de la liaison du socket" << std::endl;
            // Handle error appropriately
            return -1;
        }

        result = listen(TCP_Socket, SOMAXCONN);
        if (result == SOCKET_ERROR)
        {
            std::cerr << "Erreur lors de la mise en écoute du socket" << std::endl;
            // Handle error appropriately
            return -1;
        }

        return 0;
    }

    int ClientAcceptation()
    {
        TCP_Client = accept(TCP_Socket, NULL, NULL);
        if (TCP_Client == INVALID_SOCKET)
        {
            std::cerr << "Erreur lors de l'acceptation du client" << std::endl;
            // Handle error appropriately
            return -1;
        }
        return 0;
    }

    int MessageReception()
    {
        char buffer[4096];
        int result = recv(TCP_Client, buffer, sizeof(buffer), 0);
        if (result > 0)
        {
            buffer[result] = '\0';
            std::cout << "Nombre de caractères : " << result << std::endl;
            std::cout << "Contenu du message : " << buffer << std::endl;
        }
        else if (result == 0)
        {
            std::cerr << "La connexion a été fermée par le client" << std::endl;
        }
        else
        {
            std::cerr << "Erreur lors de la réception du message" << std::endl;
            // Handle error appropriately
        }

        return result;
    }

    int Envoi_AccuseReception()
    {
        const char *ackMessage = "Message reçu avec succès!";
        int result = send(TCP_Client, ackMessage, strlen(ackMessage), 0);
        if (result == SOCKET_ERROR)
        {
            std::cerr << "Erreur lors de l'envoi de l'accusé de réception" << std::endl;
            // Handle error appropriately
        }

        return result;
    }

    void Fin_Connexion()
    {
        closesocket(TCP_Client);
    }
};

int main(){
    Serveur_TCP serveur;
    serveur.SocketCreation();
    serveur.SocketEcoute();

    while (true)
    {
        std::cout << "Attente d'un client. Appuyez sur 'q' pour quitter." << std::endl;

        if (_kbhit()) // Check if a key is pressed
        {
            char key = _getch(); // Get the pressed key
            if (key == 'q' || key == 'Q')
            {
                std::cout << "Fermeture du serveur." << std::endl;
                break; // Exit the loop if 'q' is pressed
            }
        }
        serveur.ClientAcceptation();
        serveur.MessageReception();
        serveur.Envoi_AccuseReception();
        serveur.Fin_Connexion();
    }

    return 0;
}
