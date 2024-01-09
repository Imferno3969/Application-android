#include <iostream>
#include <winsock2.h>

#define Taille_bloc 80

// Classe représentant un objet client TCP
class Objet_ClientTCP {
private:
    const char* Nom_de_l_Objet;     // Nom de l'objet
    SOCKET MonSocket;         // Socket pour la communication
    SOCKADDR_IN MonAdresse;   // Informations sur l'adresse
    WORD VersionRequise;      // Version de Winsock requise
    WSADATA InformationsdeVersion; // Informations sur Winsock
    int ErreurdeVersion;      // Code d'erreur de version

public:
    Objet_ClientTCP(const char* Nom_de_l_Objet); // Constructeur
    ~Objet_ClientTCP();                      // Destructeur
    const char* RecupNom();                        // Récupérer le nom de l'objet
    int RecupVersion();                      // Récupérer le code d'erreur de version
    int CreationSocket();                    // Créer un socket
    int ConnexionSocket();                   // Se connecter au serveur
    int EnvoiMessage(WSABUF Message);       // Envoyer un message
    int ReceptionMessage();                  // Recevoir un message
};

// Constructeur de la classe Objet_ClientTCP
Objet_ClientTCP::Objet_ClientTCP(const char* Nom_de_l_Objet) {
    this->Nom_de_l_Objet = Nom_de_l_Objet;
    VersionRequise = MAKEWORD(2, 2);

    // Initialiser Winsock
    ErreurdeVersion = WSAStartup(VersionRequise, &InformationsdeVersion);
    if (ErreurdeVersion != 0) {
        std::cerr << "Erreur lors de l'initialisation de Winsock : " << ErreurdeVersion << std::endl;
        exit(EXIT_FAILURE);
    }
}

// Destructeur de la classe Objet_ClientTCP
Objet_ClientTCP::~Objet_ClientTCP() {
    closesocket(MonSocket); // Fermer le socket
    WSACleanup();           // Nettoyer Winsock
}

// Récupérer le nom de l'objet
const char* Objet_ClientTCP::RecupNom() {
    return Nom_de_l_Objet;
}

// Récupérer le code d'erreur de version
int Objet_ClientTCP::RecupVersion() {
    return ErreurdeVersion;
}

// Créer un socket
int Objet_ClientTCP::CreationSocket() {
    MonSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (MonSocket == INVALID_SOCKET) {
        std::cerr << "Erreur lors de la création du socket : " << WSAGetLastError() << std::endl;
        exit(EXIT_FAILURE);
    }

    MonAdresse.sin_family = AF_INET;
    MonAdresse.sin_addr.s_addr = inet_addr("127.0.0.1");
    MonAdresse.sin_port = htons(23);

    return 0;
}

// Se connecter au serveur
int Objet_ClientTCP::ConnexionSocket() {
    int result = connect(MonSocket, (struct sockaddr*)&MonAdresse, sizeof(MonAdresse));
    if (result == SOCKET_ERROR) {
        std::cerr << "Erreur lors de la connexion au serveur : " << WSAGetLastError() << std::endl;
        exit(EXIT_FAILURE);
    }

    return 0;
}

// Envoyer un message
int Objet_ClientTCP::EnvoiMessage(WSABUF Message) {
    DWORD bytesSent;
    int result = WSASend(MonSocket, &Message, 1, &bytesSent, 0, NULL, NULL);
    if (result == SOCKET_ERROR) {
        std::cerr << "Erreur lors de l'envoi du message : " << WSAGetLastError() << std::endl;
        exit(EXIT_FAILURE);
    }

    return 0;
}

// Recevoir un message
int Objet_ClientTCP::ReceptionMessage() {
    char buffer[Taille_bloc];
    DWORD bytesRead;
    int result = recv(MonSocket, buffer, Taille_bloc, 0);
    if (result == SOCKET_ERROR) {
        std::cerr << "Erreur lors de la réception du message : " << WSAGetLastError() << std::endl;
        exit(EXIT_FAILURE);
    }

    // Afficher le message reçu
    std::cout << "Message reçu : " << buffer << std::endl;

    return 0;
}

// Fonction principale
int main() {
    // Initialiser Winsock
    Objet_ClientTCP client("MonClient");

    // Créer et connecter le socket
    client.CreationSocket();
    client.ConnexionSocket();

    // Envoyer un seul message et recevoir la réponse
    {
        // Initialiser le message à envoyer
        char messageBuffer[] = "test";
        WSABUF message;
        message.buf = messageBuffer;
        message.len = sizeof(messageBuffer) - 1;  // Exclure le caractère nul

        // Envoyer le message
        client.EnvoiMessage(message);

        // Recevoir et afficher la réponse du serveur
        client.ReceptionMessage();
    }
        
    return 0;
}
