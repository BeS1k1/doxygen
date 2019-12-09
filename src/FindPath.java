import java.io.*;
import java.nio.file.*;
import java.util.*;

import javafx.util.Pair;
/**
   @class FindPath
   @brief Главный класс,
   
   который находит путь в графе, заданном матрицей смежности, из одной вершины в другую.   
   @author Николай
	
 */
public class FindPath {
	/**
	 @brief Начальная функция,
	 
	 в которой реализованно чтение из входящего файла
	 @param args
	 @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String str1;
		String str2;
		int N; //кол-во строк
		int M; //кол-во столбцов
		int[] start = new int[2];
		int[] finish = new int[2];
		String[] arr;
		String[] arr2;
		String[] arr3;
		
		
		String line;
		StringBuffer sb = new StringBuffer();
		/**
		 	@brief в блоке try происходит чтение из текстового файла output.txt
		 	Пример файла:
		 	12
			15
			1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
			1 0 0 0 0 0 0 0 0 1 0 0 0 0 1
			1 0 0 0 0 0 0 0 1 0 0 0 0 0 1
			1 0 0 0 0 0 0 1 0 1 0 1 0 1 1
			1 0 0 0 0 0 1 0 1 0 1 0 1 0 1
			1 0 0 0 0 1 0 0 0 0 0 0 0 0 1
			1 0 0 0 1 0 0 1 0 0 0 0 0 0 1
			1 0 0 1 0 1 1 1 1 1 1 1 1 1 1
			1 0 0 0 0 0 0 0 0 0 0 0 0 0 1
			1 1 0 0 1 1 0 0 0 0 0 0 0 0 1
			1 0 0 0 1 0 0 0 0 0 0 0 0 0 1
			1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
			9 3
			6 13
			
			Где в первой строке - количество строк,
			во второй - столбцов,
			далее сам граф,
			затем координаты вершин, между которыми нужно провести путь
		 */
		try (BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"))) {
			str1 = reader.readLine();
			str2 = reader.readLine();
			N = Integer.parseInt(str1);
			M = Integer.parseInt(str2);			
			for (int i = 0; i < N-1; i++) {
				line = reader.readLine();
				sb.append(line).append(" ");
			}
			line = reader.readLine();
			sb.append(line);
			line = sb.toString();
			str1 = reader.readLine();
			str2 = reader.readLine();
			reader.close();
		}
		arr = line.split(" ");
		arr2 = str1.split(" ");
		arr3 = str2.split(" ");
		int[][] maze= new int[N][M];
		int[] intNum = new int[N*M];
		
		for (int i = 0; i < arr.length; i++) {
			intNum[i]= Integer.parseInt(arr[i]);
        }
		for (int i = 0; i < 2; i++) {
			start[i]=Integer.parseInt(arr2[i]);
			finish[i]=Integer.parseInt(arr3[i]);
        }
		int count = 0;
		for (int i = 0; i < N; i++) 
        { 
            for (int j = 0; j < M; j++) 
            { 
            	maze[i][j]=intNum[count];
            	count++;
            }
        }
		/**
			@brief получаем ответ в функции findpath
		 */
		ArrayList<Pair<Integer, Integer>> result = findPath(start, finish, maze, N, M);
		FileWriter fileWriter = new FileWriter("output.txt"); 
		System.out.println(result);
		if (result.size()<=1) {
			fileWriter.write("path not found");
		}
		else {
			for (int i = result.size()- 1; i>=0 ; i--) {
				fileWriter.write(result.get(i).getKey()+" "+ result.get(i).getValue());
				fileWriter.append("\r\n");
			}
		}
		
		fileWriter.close(); 

	}
	/**
	 	@brief Функция, которая возвращает путь в графе между двумя вершинами, если не существует, то выводит "path not found"
	 	
	  	@param start координаты первой вершины
	  	@param finish координаты второй вершины
	  	@param maze сам граф, заданный двумерной матрицей
		@param N количество строк
	  	@param M количество столбцов
	  	@return возвращает путь, если он существует
	 */
	public static ArrayList<Pair<Integer, Integer>> findPath(int[] start, int[] finish, int[][] maze, int N, int M){
		Map<Pair<Integer, Integer>, Pair<Integer, Integer>> track = new LinkedHashMap<Pair<Integer, Integer>, Pair<Integer, Integer>>();
		Queue<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
		queue.offer(new Pair<>(start[0], start[1]));
		while (queue.peek() != null) {
			Pair<Integer, Integer> pair = queue.remove();
			if (pair.getKey() < 1 || pair.getKey() > N || pair.getValue() < 1 || pair.getValue() > M) continue;
			if (maze[pair.getKey()-1][ pair.getValue()-1] != 0) continue;

			for (int dy = -1; dy <= 1; dy++)
				for (int dx = -1; dx <= 1; dx++)
					if (dx != 0 && dy != 0) continue;
					else {
						Pair<Integer, Integer> pair2 = new Pair<> ( pair.getKey() + dx, pair.getValue() + dy);
						if (pair.getKey().equals(pair2.getKey()) && pair.getValue().equals(pair2.getValue()) ) continue;
						if (maze[pair2.getKey()-1][ pair2.getValue()-1] != 0) continue;
						if (track.containsKey(pair2)) continue;
						track.put(pair2, pair);
						queue.offer(pair2);
					}
			Pair<Integer, Integer> pair2 = new Pair<>(finish[0], finish[1]);
			if (pair.getKey().equals(pair2.getKey()) && pair.getValue().equals(pair2.getValue()) ) break;
		}
		ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
		
		Pair<Integer, Integer> pathItem = new Pair<>(finish[0], finish[1]);
		Pair<Integer, Integer> startPoint = new Pair<>(start[0], start[1]);
		
		while (pathItem != null) {
			result.add(pathItem);
			pathItem = track.get(pathItem);
			if (pathItem == null)
				break;	
			if (pathItem.getKey().equals(startPoint.getKey()) && pathItem.getValue().equals(startPoint.getValue()) ) {
				result.add(pathItem);
				break;
			}
		}
		
		return result;
		
	}
}
