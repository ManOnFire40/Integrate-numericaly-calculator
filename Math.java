package Math_assigment;

import java.util.ArrayList;

public class Math_Assigment {
	 
	static boolean start_with(String func) {
		boolean flag = false;
		String n1 = func.charAt(0) + "";
		String n2 = func.charAt(1) + "";
		if (n1.equals("x") && n2.equals("^"))
			flag = true;
		return flag;
	}
	public static double eval_one_func(String func, double x) {
		double res = 0;
		if (func.equals("e^x"))
			res += (Math.exp(x));
		if (func.equals("ln(x)"))
			res += (Math.log(x));
		if (func.equals("sin(x)"))
			res += (Math.sin(x));
		if (func.equals("cos(x)"))
			res += (Math.cos(x));
		if (func.equals("x^(1/2)"))
			res += (Math.sqrt(x));
		if (start_with(func)) {
			String m = func.substring(2);
			int power = Integer.parseInt(m);
			res += (Math.pow(power, x));
		}
		return res;
	}
	public static double eval_basic(String operator, double operand1, double operand2) {
		double res = 0;
		if (operator.equals("*"))
			res = operand1 * operand2;
		if (operator.equals("+"))
			res = operand1 + operand2;
		return res;
	}
	public static double[] eval_to_end(ArrayList<String> spliting_funcs, ArrayList<String> operators,
			double[] curr_step) {
	
		double result=0;

		double[] res_at_step = new double[curr_step.length];
		// to not destroy operators
		ArrayList<String> temp_operators = new ArrayList<String>();
		for (int i = 0; i < operators.size(); i++) {
			temp_operators.add(operators.get(i));
		}
		
		ArrayList<String> temp_spliting_funcs= new ArrayList<String>();
		for (int i = 0; i < spliting_funcs.size(); i++) {
			temp_spliting_funcs.add(spliting_funcs.get(i));
		}

		// at each step value
		for (int i = 0; i < curr_step.length; i++) {
			// evaluation of functions
			ArrayList<Double> current_eval = new ArrayList<Double>();
			for (int j = 0; j < temp_spliting_funcs.size(); j++) {
		//		System.out.println(spliting_funcs.get(j));
					// 
				double curr_step_evalution = eval_one_func(temp_spliting_funcs.get(j), curr_step[i]);
				current_eval.add(curr_step_evalution);
			}
	

		
			while (current_eval.size() > 1 &&!temp_operators.isEmpty()) {
				int prirorty_index = get_highest_priroty_index(temp_operators);
				String operat = operators.get(prirorty_index);
				double operand1 = current_eval.get(prirorty_index);
				double operand2 = current_eval.get(prirorty_index + 1);
			//if(!temp_operators.isEmpty())
			//	if(current_eval.size() > 1)
				current_eval.remove(prirorty_index );
				current_eval.remove(prirorty_index);
				temp_operators.remove(prirorty_index);
			
				double x = eval_basic(operat, operand1, operand2);
				current_eval.add(prirorty_index, x);
			}
			if(current_eval.size()==1)
			 result = current_eval.get(0);
			res_at_step[i] = result;
		}
		return res_at_step;
	}
	public static int get_highest_priroty_index(ArrayList<String> operators) {
		int priroty = 0;
		for (int i = 0; i < operators.size(); i++) {
			if (operators.get(i).equals("*"))
				priroty = i;
		}
		return priroty;
	}
	public static double Integrate_numericaly(double upper_bound, double lower_bound, String func, double no_of_steps) {
		// Math.toRadians();
		double h = (upper_bound - lower_bound) / no_of_steps;
		ArrayList<String> spliting_funcs = new ArrayList<String>();
		ArrayList<String> operators = new ArrayList<String>();
		;
		double[] curr_step = new double[(int) no_of_steps + 1];
		double[] res_at_step = new double[(int) no_of_steps + 1];

		double returned = 0;
		double curr = lower_bound;
		for (int i = 0; i < curr_step.length; i++) {
			
			curr_step[i] = curr;
			curr += h;
		}
		
	
		String curr1 = "";
		for (int i = 0; i < func.length(); i++) {

			
			
			 if (!((func.charAt(i) + "").equals("*") || (func.charAt(i) + "").equals("+"))){
					curr1 += (func.charAt(i) + "");
				}
				
			
			if (((func.charAt(i) + "").equals("*") || (func.charAt(i) + "").equals("+"))|| i==func.length()-1) {
				spliting_funcs.add(curr1);
				if(((func.charAt(i) + "").equals("*") || (func.charAt(i) + "").equals("+")))
				operators.add((func.charAt(i) + ""));
				
				curr1 = "";
			}
		}
		

		
		for (int i = 0; i < res_at_step.length; i++) {
			res_at_step[i] = eval_to_end(spliting_funcs, operators, curr_step)[i];
		}
		if (no_of_steps % 2 == 0) // 1/3 simpson
		{
			double step0 = res_at_step[0];
			double stepN = res_at_step[res_at_step.length - 1];
			double step_odd = 0;
			double step_even = 0;
			for (int i = 1; i < res_at_step.length - 1; i++) {
				if (i % 2 == 0)
					step_even += res_at_step[i];
				if (i % 2 == 1)
					step_odd += res_at_step[i];
			}

			returned = (h / 3) * (step0 + stepN + (4 * step_odd) + (2 * step_even));
		}

		if (no_of_steps % 3 == 0) // 3/8 simpson
		{
			double step0 = res_at_step[0];
			double stepN = res_at_step[res_at_step.length - 1];
			double Modulus3 = 0;
			double rest = 0;
			for (int i = 1; i < res_at_step.length - 1; i++) {
				if (i % 3 == 0)
					Modulus3 += res_at_step[i];
				else
					rest += res_at_step[i];
			}
			returned = (3 * h / 8) * (step0 + stepN + (2 * Modulus3) + (2 * rest));
		} else // trapazoid
		{
			double step0 = res_at_step[0];
			double stepN = res_at_step[res_at_step.length - 1];
			double in_middle = 0;
			for (int i = 1; i < res_at_step.length - 1; i++) {
				in_middle += res_at_step[i];
			}
			returned = (h / 2) * (step0 + stepN + (2 * in_middle));
		}
		return returned;
		
	}
	public static void main(String[] args) {
		double x = Integrate_numericaly(1.2, 0, "e^x+cos(x)", 6);
		System.out.println(x);
	}
}
