import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    public IUDoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);

        if (size > 0) { // or head != null, or !isEmpty()
            head.setPrev(newNode);
        } else {
            tail = newNode;
        }

        newNode.setNext(head);
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        // recommend writing normally first, then once ListIter is completely correct
        // comment out old method and try with ListIters
        // no point difference using ListIters or normal method writing

        if (isEmpty()) {
            addToFront(element);
        } else {
            Node<T> newNode = new Node<T> (element);
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
            size++;
            modCount++;
        }

        
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> currNode = head;

        while (currNode.getNext() != null && currNode.getElement() != target) { // should end at currNode = target
            currNode = currNode.getNext();
        }

        if (currNode.getElement() != target) { // checks to see if it went all the way thru the list without finding target
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<T>(element);

        newNode.setNext(currNode.getNext()); // set nN next

        if(newNode.getNext() != null) { // checks to see if its the last element in the list
            newNode.getNext().setPrev(newNode); // set next nodes prev to the new node
        } else { // if it is the last element, its the new tail
            tail = newNode;
        }
        newNode.setPrev(currNode);  // set the nN prev to currNode
        currNode.setNext(newNode);  // finally set the currNode next to newNode.
        
        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) { 
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currNode;
        Node<T> newNode = new Node<T>(element);
        
        // add special case for i = 0, i = size
        if(index == 0) {
            addToFront(element);
            return;
        } else if(index == size) {
            addToRear(element);
            return;
        }


        // general case, just split to reduce coefficient on O(n)
        if (index < size/2) {

            currNode = head;

            for (int i = 0; i < index; i++) {   // should end where currNode is at index
                currNode = currNode.getNext();
            }

            newNode.setNext(currNode.getNext()); // get node at index + 1
            newNode.setPrev(currNode); // put nN prev to curr
            newNode.getNext().setPrev(newNode); // set node's at index + 1 Prev() to new Node
            currNode.setNext(newNode); // end by putting curr's next node to nN

        } else {
            
            currNode = tail;

            for (int i = size; i > index; i--) { // have to make sure its i--, messed up first with that
                currNode = currNode.getPrev();  
            }

            // for loop ends at currNode = node at index - 1, so still need to add newNode after
            // newNode.setPrev(currNode.getPrev());
            // newNode.getPrev().setNext(newNode); 
            // newNode.setNext(currNode);
            // currNode.setPrev(newNode);


            newNode.setNext(currNode.getNext()); 
            newNode.setPrev(currNode); 
            newNode.getNext().setPrev(newNode); 
            currNode.setNext(newNode); 
        }

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        ListIterator<T> itr = listIterator();
        
        T retVal = itr.next();
        itr.remove();

        return retVal;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T retVal = tail.getElement();

        if (size > 1) {
            tail.getPrev().setNext(null);
        } else {
            head = null;
        }

        tail = tail.getPrev();
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T remove(T element) {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> currNode = head;

        while (currNode.getNext() != null && currNode.getElement() != element) { // should end at currNode = element's node
            currNode = currNode.getNext();
        }

        if (currNode.getElement() != element) { // checks to see if it went all the way thru the list without finding target
            throw new NoSuchElementException();
        }

        T retVal = currNode.getElement();

        if (retVal != element) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nfailed getting RetVal !!!!!!!!\n\n\n\n\n\n\n\n\n");
        }
        
        if (currNode == head){
            return removeFirst();
        } else if (currNode == tail) {
            return removeLast();
        } else {
            Node<T> prevNode = currNode.getPrev();
            Node<T> nextNode = currNode.getNext();

            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
        
        size--;
        modCount++;

        return retVal;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currNode;
        
        
        // add special case for i = 0, i = size-1
        if(index == 0) {
            return removeFirst();
        } else if(index == size-1) {
            return removeLast();
        }

        T retVal = null;
        // general case, just split to reduce coefficient on O(n)
        if (index < size/2) {

            currNode = head;

            for (int i = 0; i < index; i++) {   // should end where currNode is at index
                currNode = currNode.getNext();
            }

            retVal = currNode.getElement();

            Node<T> prevNode = currNode.getPrev();
            Node<T> nextNode = currNode.getNext();

            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);

        } else {
            
            currNode = tail;

            for (int i = size; i > index; i--) { 
                currNode = currNode.getPrev();  
            }

            retVal = currNode.getElement();

            Node<T> prevNode = currNode.getPrev();
            Node<T> nextNode = currNode.getNext();

            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
            
        }

        size--;
        modCount++;
        return retVal;


        ////// from class
        // Node<T> currNode = head;

        // for(int i = 0; i < index; i++) {
        //     currNode = currNode.getNext();
        // }

        // if(index != 0) {    // or currNode != head
        //     currNode.getPrev().setNext(currNode.getNext());
        // } else {
        //     head = currNode.getNext();
        //     currNode.getNext().setPrev(null);
        // }

        // if (index != size -1) {
        //     currNode.getNext().setPrev(currNode.getPrev());
        // } else {
        //     tail = currNode.getPrev();
        //     currNode.getPrev().setNext(null);
        // }


        // size--;
        // modCount++;

        // return currNode.getElement();
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        // if(index == 0) {
        //     head.setElement(element);
        // }
        // if(index == size - 1) {
        //     tail.setElement(element);
        // }

        Node<T> currNode;

        if (index < size / 2) {
            currNode = head;

            for (int i = 0; i < index; i++) {
                currNode = currNode.getNext();
            }
            currNode.setElement(element);
        } else {
            currNode = tail;

            for (int i = size - 1; i > index; i--) {
                currNode = currNode.getPrev();
            }
            currNode.setElement(element);
        }

        modCount++;

    }

    @Override
    public T get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        Node<T> currNode;

        if (index < size / 2) {
            currNode = head;

            for (int i = 0; i < index; i++) {
                currNode = currNode.getNext();
            }
        } else {
            currNode = tail;

            for (int i = size - 1; i > index; i--) {
                currNode = currNode.getPrev();
            }
        }

        return currNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        int retIndex = -1;

        int i = 0;
        Node<T> currNode = head;

        while (currNode != null && retIndex < 0) {
            if (currNode.getElement().equals(element)) {
                retIndex = i;
            } else {
                currNode = currNode.getNext();
                i++;
            }

        }

        return retIndex;
    }

    @Override
    public T first() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");

        for (T element : this) {
            str.append(element.toString());
            str.append(", ");
        }

        // remove trailing comma
        if (str.length() > 1) { // !isEmpty or size() > 0 also works
            str.delete(str.length() - 2, str.length()); // str.length() - 2, is starting index to delete, str.length()
                                                        // is ending index to delete (Exclusive)
        }

        // this toString() works with ALL collections as all require Iterators
        str.append("]");
        return str.toString();

    }

    private class DLLIterator implements ListIterator<T> {
        // dont keep a prevNode reference
        private Node<T> nextNode;
        private int nextIndex;
        private int iterModCount;
        private Node<T> lastRetNode;

        // private boolean canRmvNext; // don't do this because he said its the most
        // complicated

        public DLLIterator() {
            // uses the other constructor to reduce code-duplication, not too important
            // for this contstructor, but future ones
            this(0);
        }

        // Initializes BEFORE the specified index, so next() would return El at
        // startIndex
        public DLLIterator(int startIndex) {

            // at size is fine, would just put the iterator after last element so next()
            // would return null, hasNext() == false
            if (startIndex < 0 || startIndex > size) {
                throw new IndexOutOfBoundsException("Invalid start index");
            }

            nextNode = head;
            // want it to = startIndex to not add 1n to the for loop
            nextIndex = startIndex;
            iterModCount = modCount;

            // Advance to the starting index
            for (int i = 0; i < startIndex; i++) { // only efficient in 1st half of list
                nextNode = nextNode.getNext();
            }
            lastRetNode = null;

        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T retVal = nextNode.getElement();

            lastRetNode = nextNode; // sets it to the node with retVal before moving next Node

            nextNode = nextNode.getNext();
            nextIndex++;

            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != head; // nextIndex > 0 works, but hard to keep track.
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            if (nextNode != null) {
                // this wouldn't work on tail since there is no nextNode without checking
                nextNode = nextNode.getPrev();
            } else {
                nextNode = tail;
            }

            lastRetNode = nextNode; // sets it to the node
            nextIndex--;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void set(T e) {
            if(iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRetNode == null) {
                throw new IllegalStateException();
            }

            lastRetNode.setElement(e);

            modCount++;
            iterModCount++;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            // Find the insertion point
            Node<T> newNode = new Node<T>(e);

            if(tail != null) {
                tail.setNext(newNode);
                newNode.setPrev(tail);
            }
            
            
            tail = newNode;
            

            size++;
            modCount++;
            iterModCount++;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRetNode == null) {
                throw new IllegalStateException();
            }

            if (lastRetNode != head) {
                lastRetNode.getPrev().setNext(lastRetNode.getNext());
            } else {
                head = lastRetNode.getNext();
            }
            if (lastRetNode != tail) {
                lastRetNode.getNext().setPrev(lastRetNode.getPrev());
            } else {
                tail = lastRetNode.getPrev();
            }

            if (lastRetNode != nextNode) { // last move was next()
                nextIndex--; // fewer elements to the left that were there
            } else { // last move was previous()
                nextNode = lastRetNode.getNext(); // nextNode can't point at a removed node
            }

            lastRetNode = null;

            size--;
            
            modCount++;
            iterModCount++;
        }

    }
}
