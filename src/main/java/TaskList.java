import java.util.ArrayList;

/**
 * Represents the list of tasks in the application and
 * provides operations to manipulate the list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list using an existing list of tasks.
     *
     * @param tasks existing tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the given index.
     *
     * @param index index of task to delete
     * @return deleted task
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as done.
     *
     * @param index index of task to mark
     */
    public void markTask(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the given index as not done.
     *
     * @param index index of task to unmark
     */
    public void unmarkTask(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns the task at the given index.
     *
     * @param index index of task
     * @return task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task list size
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying task list for storage operations.
     *
     * @return list of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     *
     * @param keyword keyword to search for
     * @return task list containing matching tasks
     */
    public TaskList find(String keyword) {
        TaskList matches = new TaskList();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matches.addTask(task);
            }
        }

        return matches;
    }
}