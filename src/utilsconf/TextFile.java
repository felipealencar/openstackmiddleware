package utilsconf;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class TextFile {

    public static final String[] PONCTUATION = {".", "!", "?", ";", ",", ":", "(", ")", "`", "~", "@", "#", "$", "%", "^", "&", "*", "\\", "/",
        "[", "]", "{", "}", "<", ">", "|", "-", "=", "+", "\"", "'"};

    private Writer out;
    private char[] charArray;
    private boolean readOnly;
    private int currentPosition = -1;
    private final File file;

    /**
     * Construtor da classe com dois argumentos. Se o caminho nao existir,
     * o arquivo nao sera criado.
     *
     * @param name o nome do arquivo
     * @param readOnly indica se e' para abrir somente leitura
     */
    public TextFile(String name, boolean readOnly) throws IOException {
        if(name == null)
            throw new IllegalArgumentException("Name can't be null!");
        
        this.readOnly = readOnly;
        this.file = new File(name);

        if(readOnly){
            currentPosition = 0;
            this.charArray = getContent().toCharArray();
        }

        createTextFile();
    }

    /**
     * Construtor da classe com dois argumentos. Se o caminho nao existir,
     * o arquivo nao sera criado. O Arquivo e' aberto no modo de escrita.
     *
     * @param name o nome do arquivo
     */
    public TextFile(String name) throws IOException {
        this(name, false);
    }

    /**
     * Construtor da classe.
     *
     * @param file o arquivo a ser aberto
     */
    public TextFile(File file) throws IOException{
        if(file == null)
            throw new IllegalArgumentException("File can't be null!");

        this.file = file;
    }

    /**
     * Construtor da classe que permite manipular o conteudo do array
     * utilizando alguns metodos da classe.
     *
     * @param text o array de caracteres
     */
    public TextFile(char text[]){
        file = null;
        this.charArray = text;
        readOnly = true;
    }

    /**
     * Construtor da classe.
     *
     * @param file o arquivo a ser aberto
     * @param readOnly indica se e' para abrir somente leitura
     */
    public TextFile(File file, boolean readOnly) throws IOException{
        this(file.toString(), readOnly);
    }
    
    /**
     * Retorna o nome do arquivo texto.
     *
     * @return um string com o nome do arquivo texto
     */
    public String getPath() {
        return file.getAbsolutePath();
    }
    
    /**
     * Salva um texto qualquer no arquivo texto e pula uma linha.
     *
     * @param text o texto a ser salvo no arquivo
     */
    public void addContent(String text) throws IOException, IllegalAccessException {
        if(readOnly)
            throw new IllegalAccessException("Mode read only!");

        try{
            openFlow();
            out.write(String.format("%s", text));
            closeFlow();
        }catch(IOException e){
            closeFlow();
            throw e;
        }//Fim do catch
    }
    
    /**
     * Abre o fluxo do arquivo. Este método deve ser chamado antes de escrever
     * no arquivo texto.
     */
    private void openFlow() throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
    }
    
    /**
     * Fecha o fluxo do arquivo aberto. Este método deve ser chamado antes do objeto ser
     * destruído, caso contrário as modificações no arquivo serão perdidas
     */
    private void closeFlow() throws IOException {
        out.close();
    }
    
    /**
     * Retorna o conteúdo do arquivo texto.
     *
     * @return uma String com todo o conteúdo do arquivo texto
     */
    public String getContent() throws FileNotFoundException {
        StringBuilder aux = new StringBuilder();
        Scanner arquivo = new Scanner(new FileInputStream(file), "ISO-8859-1");

        while (arquivo.hasNext()) {
            aux.append(arquivo.nextLine()).append("\n");
        }

        arquivo.close();
        return aux.toString();
    }
    
    /**
     * Apaga o conteúdo do arquivo texto.
     */
    public void eraseContent() throws IOException, IllegalAccessException{
        if(readOnly)
            throw new IllegalAccessException("Mode read only!");
        
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
        closeFlow();
    }
    
    /**
     * Seta o conteudo do arquivo apagando o anterior.
     *
     * @param content o conteudo a ser setado.
     */
    public void setContent(String content) throws IOException, IllegalAccessException {
        if(readOnly)
            throw new IllegalAccessException("Mode read only!");

        if(content == null)
            content = "";

        eraseContent();
        addContent(content);
    }

    /**
     * Cria o arquivo se ele nao existir.
     */
    public void createTextFile() throws IOException{
        String name = file.getAbsolutePath();
        
        if(name.contains(File.separator)){
            int lastSeparator = name.lastIndexOf(File.separator);

            String path = name.substring(0, lastSeparator);
            File file = new File(path);

            if(!file.exists())
                file.mkdirs();
        }

        openFlow();
        closeFlow();
    }

    /**
     * Retorna o proximo char a ser lido. Para chamar esse metodo
     * o arquivo tem que ter sido aberto no modo somente leitura.
     *
     * @return o proximo char
     */
    public char nextChar() throws EOFException, IllegalAccessException{
        if(!readOnly)
            throw new IllegalAccessException("Mode not is read only!");

        if(currentPosition == charArray.length)
            throw new EOFException("End of file reached!");

        return charArray[currentPosition++];
    }

    /**
     * Retorna o char da posicao passada como argumento. Para chamar esse metodo
     * o arquivo tem que ter sido aberto no modo somente leitura.
     *
     * @param position a posicao a ser procurada
     *
     * @return o char da posicao
     */
    public char getChar(int position) throws IllegalAccessException{
        if(!readOnly)
            throw new IllegalAccessException("Mode not is read only!");

        return charArray[position];
    }

    /**
     * Conta a quantidade de chars do arquivo. Para chamar esse metodo
     * o arquivo tem que ter sido aberto no modo somente leitura.
     *
     * @return a quantidade de chars do arquivo
     */
    public int countChars() throws IllegalAccessException{
        if(!readOnly)
            throw new IllegalAccessException("Mode not is read only!");

        return charArray.length;
    }

    /**
     * Retorna se o arquivo foi aberto no modo somente leitura
     *
     * @return verdadeiro se foi aberto no modo somente leitura e falso caso contrario
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Retorna a posicao atual da leitura
     *
     * @return a posicao atual da leitura ou -1 caso o arquivo nao tenha sido aberto no modo somente leitura
     */
    public int getAtualPosition() {
        return currentPosition;
    }

    /**
     * Seta a posicao atual da leitura. Para chamar esse metodo
     * o arquivo tem que ter sido aberto no modo somente leitura.
     *
     * @param position a posicao atual da leitura (deve esta dentro dos limites)
     */
    public void setReadPosition(int position) throws IllegalAccessException{
        if(!readOnly)
            throw new IllegalAccessException("Mode not is read only!");

        if(position < 0 || position > countChars())
            throw new IllegalArgumentException("Position out of limit!");

        this.currentPosition = position;
    }

    /**
     * Retorna se ainda ha caracteres a serem lidos. Para chamar esse metodo
     * o arquivo tem que ter sido aberto no modo somente leitura.
     *
     * @return verdadeiro se ainda ha caracteres e falso caso contrario
     */
    public boolean hasNextChars() throws IllegalAccessException{
        if(!readOnly)
            throw new IllegalAccessException("Mode not is read only!");

        return getAtualPosition() < countChars();
    }

    /**
     * Retorna a extensao do arquivo
     *
     * @return a extensao do arquivo ou null se nenhum arquivo foi aberto
     */
    public String getExtension(){
        if(file == null)
            return null;
        
        String name = file.getAbsolutePath();
        return name.substring(name.lastIndexOf(".") + 1, name.length());
    }

    /**
     * Tokeniza o conteudo.
     *
     * @return a lista de tokens
     */
    public String[] tokenizer() throws FileNotFoundException{
        String content = getContent();

        for (String ponctuation : PONCTUATION) 
            content = content.replace(ponctuation, String.format(" %s ", ponctuation));

        return content.split(" ");
    }

    /**
     * Retorna o tamanho atual do arquivo
     *
     * @return o tamanho atual do arquivo
     */
    public long size(){
        return file.length();
    }

    /**
     * Elimina os espacos laterais do conteudo.
     */
    public void trim() throws FileNotFoundException, IOException, IllegalAccessException {
        setContent(getContent().trim());
    }

}
