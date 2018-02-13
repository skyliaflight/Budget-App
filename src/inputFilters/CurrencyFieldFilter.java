package inputFilters;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class CurrencyFieldFilter extends DocumentFilter{

	public CurrencyFieldFilter(){
		super();
	}
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
	throws BadLocationException
	{
		System.out.println("We inserted.");
		super.insertString(fb, offset, string, attr);
	}
	
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
	throws BadLocationException
	{
		System.out.println("We removed.");
		super.remove(fb, offset, length);
	}
	
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attr)
	throws BadLocationException
	{
		System.out.println("We replaced.");
		//if(isInteger(text)){
		super.replace(fb, offset, length, text, attr);
		//}
	}
	
}
